package org.awesley.infra.annotation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import org.springframework.cglib.core.ProcessSwitchCallback;
import org.springframework.context.annotation.Bean;

@SupportedAnnotationTypes(value = {
	"org.awesley.infra.annotation.Customizable",
	"org.awesley.infra.annotation.CustomizableBean",
	"org.springframework.context.annotation.Bean"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CustomizationAnnotationProcessor extends AbstractProcessor {

	private ProcessingEnvWrapper processingEnvWrapper = null;
	private ContextWrapper contextWrapper;
	private OptionsWrapper options;
	private LogWrapper log;
	private Elements elementUtils;
	private Types typeUtils;
	private Boolean onlyWarn;
	private Filer filer;
	private Layer layer;
	
	private URLClassLoader refTypesClassLoader = null;
	
	private Map<String, String> loadedJars = new HashMap<>();
	private Map<String, Class<?>> referenceClassesMap = new HashMap<>();
	private Map<BeanIdentification, BeanIdentification> customizableBeans = new HashMap<>();
	private Map<BeanIdentification, BeanIdentification> nonCustomizableBeans = new HashMap<>();
	private Map<String, String> propertiesMap;
	private String[] prefixes;

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		try {
			this.processingEnvWrapper = new ProcessingEnvWrapper(processingEnv);
			this.contextWrapper = processingEnvWrapper.getContext();
			this.options = contextWrapper.getOptionsWrapper();
			this.log = contextWrapper.getLogWrapper(processingEnv.getMessager());

			this.elementUtils = processingEnv.getElementUtils();
			this.typeUtils = processingEnv.getTypeUtils();
			this.onlyWarn = Boolean.parseBoolean(processingEnv.getOptions().getOrDefault("custAnnotationWarn", "false"));
			this.filer = processingEnv.getFiler();
			
			loadCustomizationLayerProperties();
		    this.layer = getLayer();
		    this.prefixes = getPrefixes();
			
			createURLClassLoaderForRefTypes(processingEnv);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		try {
			log.printMessage(Kind.NOTE, "Customization Annotation");
			System.out.println("INFO: Processing Customization Annotations");
			
			if (layer == Layer.CORE) {
				return false;
			}
			
	//		roundEnv.getElementsAnnotatedWith(Bean.class);
			
			for (TypeElement annotation : annotations) {
				System.out.println("INFO: Processing annotation: " + annotation.getSimpleName());
				Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
				for (Element element : elements) {
					ExecutableElement methodElement = (ExecutableElement) element;
					TypeElement containingClassElement = (TypeElement)methodElement.getEnclosingElement();
					PackageElement packageElement = (PackageElement) containingClassElement.getEnclosingElement();
					
					System.out.println("INFO: annotated class name: " + containingClassElement.getSimpleName());
					System.out.println("INFO: package is (qualified): " + packageElement.getQualifiedName());
					
					Bean annotationInstance = element.getAnnotation(Bean.class);
					BeanIdentification candidateBeanIdentification = getBeanIdentification(annotationInstance, methodElement);
					typeUtils.erasure(methodElement.getReturnType());
					nonCustomizableBeans.values().stream().filter(cn -> cn.getBeanName().contains("entity1")).collect(Collectors.toList()).get(1).hashCode();
					candidateBeanIdentification.hashCode();
					if (nonCustomizableBeans.containsKey(candidateBeanIdentification) && !customizableBeans.containsKey(candidateBeanIdentification)) {
						log.printMessage(Kind.ERROR, "In class: " + containingClassElement + 
								" Bean: " + candidateBeanIdentification.getBeanName() + 
								" can't override non-customizable Bean!", element);
						
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}

//	private void loadAndAnalyzeReferenceJar(String fullyQualifiedBeanName) {
//		int indexOfLastDot = fullyQualifiedBeanName.lastIndexOf(".");
//		String packageName = fullyQualifiedBeanName.substring(0, indexOfLastDot);
//		String className = fullyQualifiedBeanName.substring(indexOfLastDot + 1) + ".class";
//		
//		String jarFileName = getJarFileName(packageName, className);
//		if (!loadedJars.containsKey(jarFileName)) {
//			JarFile jarFile = loadJarFile(jarFileName);
//			URLClassLoader cl = getClassLoaderForJar(jarFileName);
//			loadAndAnalyzeClassesFromJar(jarFile, cl);
//			
//			loadedJars.put(jarFileName, jarFileName);
//		}
//	}

//	private String getJarFileName(String packageName, String className) {
//		String foName = null;
//		/*
//		 * PackageElement coreTopPackageElement = (PackageElement)
//		 * typeUtils.asElement(beanType).getEnclosingElement(); List<? extends Element>
//		 * a = coreTopPackageElement.getEnclosedElements();
//		 */
//		foName = getResourceName(packageName, className);
//
//		String jarFileName = foName.substring(0, foName.indexOf("("));
//		return jarFileName;
//	}

//	private String getResourceName(String packageName, String className) {
//		String resourceName = null;
//		try {
//			FileObject fo = filer.getResource(StandardLocation.CLASS_PATH, packageName, className);
//			resourceName = fo.getName();
//		} catch (IOException e) {}
//		return resourceName;
//	}
	
//	private URLClassLoader getClassLoaderForJar(String jarFileName) {
//		URL[] urls = getJarURL(jarFileName);
//		URLClassLoader cl = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());
//		return cl;
//	}

//	private URL[] getJarURL(String jarFileName) {
//		URL[] urls = null;
//		try {
//			URL[] urls1 = { new URL("jar:file:" + jarFileName + "!/") };
//			urls = urls1;
//		} catch (IOException e3){}
//		return urls;
//	}

	private void loadCustomizationLayerProperties() {
		List<String> lines = new ArrayList<>();
		try {
			FileObject propertiesFile = filer.getResource(StandardLocation.CLASS_OUTPUT, "", "layer.properties");
		    try (Reader reader = propertiesFile.openReader(false)) {
		        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
		            lines.addAll(bufferedReader.lines().collect(Collectors.toList()));
		        }
		    } catch (IOException e) {
		        // Just ignore because reading from a non-existing file and there is no way to detect its existence
		        // than open the input stream/reader.
		    }
			
		} catch (IOException e) {}
		
		propertiesMap = lines.stream()
			.map(s -> s.split("="))
			.filter(sa -> sa != null && sa.length == 2)
			.collect(Collectors.toMap(pair -> pair[0], pair -> pair[1]));
	}
	
	private Layer getLayer() {
		Layer layer = Layer.valueOfLabel(propertiesMap.get("customization.layer"));
		return layer;
	}

	String[] getPrefixes() {
		String packagePrefixesStr = propertiesMap.get("customization.packageprefixes");
		if (packagePrefixesStr != null && !packagePrefixesStr.isEmpty()) {
			return packagePrefixesStr.split(";");
		}
		
		return null;
	}
	
	private void createURLClassLoaderForRefTypes(ProcessingEnvironment processingEnv) {
		String classpath = getProcessingEnvCompilationClasspath(processingEnv);
		refTypesClassLoader = initRefTypesClassLoader(classpath);
	}

	private String getProcessingEnvCompilationClasspath(ProcessingEnvironment processingEnv) {
		String classpath = options.getOption("-classpath");
		return classpath;
	}

	private URLClassLoader initRefTypesClassLoader(String classpath) {
		String[] classPathLocations = Arrays.stream(classpath.split(";"))
				.filter(p -> p.endsWith(".jar"))
				//.filter(p -> !p.contains("infra-customization"))
				.collect(Collectors.toList()).toArray(new String[0]);
		List<URL> urls = Arrays.stream(classPathLocations)
			.map(cpl -> {
				try {
					return new URL("jar:file:" + cpl + "!/");
				} catch (Throwable e) {
					e.printStackTrace();
				}
				return null;
			}).collect(Collectors.toList());
		
		URLClassLoader ucl = new URLClassLoader(urls.toArray(new URL[0]), CustomizationAnnotationProcessor.class.getClassLoader());
		
		for (String cpl : classPathLocations) {
			JarFile jarFile = loadJarFile(cpl);
			loadAndAnalyzeClassesFromJar(jarFile, ucl);
		}
		return ucl;
	}
	
	private JarFile loadJarFile(String jarFileName) {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jarFileName);
		} catch (IOException e2) {}
		return jarFile;
	}

	private void loadAndAnalyzeClassesFromJar(JarFile jarFile, URLClassLoader cl) {
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {
		    JarEntry je = e.nextElement();
		    if(!isClassResource(je)){
		        continue;
		    }
		    String loadedClassName = classNameFromResource(je);
		    if (matchAnyPrefix(loadedClassName)) {
			    Class<?> c = loadClass(cl, loadedClassName);
			    analyzeClass(c);
		    }
		}
	}

	private boolean matchAnyPrefix(String loadedClassName) {
		return Arrays.stream(prefixes)
			.anyMatch(p -> loadedClassName.startsWith(p));
	}

	private boolean isClassResource(JarEntry je) {
		return !je.isDirectory() && je.getName().endsWith(".class");
	}

	private String classNameFromResource(JarEntry je) {
	    // -6 because of .class
		String loadedClassName = je.getName().substring(0,je.getName().length()-6);
		loadedClassName = loadedClassName.replace('/', '.');
		return loadedClassName;
	}

	private Class<?> loadClass(URLClassLoader cl, String loadedClassName) {
		Class<?> c = null;
		try {
			if (!referenceClassesMap.containsKey(loadedClassName)) {
				c = cl.loadClass(loadedClassName);
				referenceClassesMap.put(loadedClassName, c);
			}
		} catch (Throwable e1) { e1.printStackTrace(); }
		
		return c;
	}

	private void analyzeClass(Class<?> c) {
		try {
			Method[] methods = c.getMethods();
			for (Method m : methods) {
				analyzeMethod(m);
			}
		} catch (Throwable t) { t.printStackTrace(); }
	}

	private void analyzeMethod(Method m) {
		analyzeCustomizableBean(m);
		analyzeBean(m);
	}

	private void analyzeCustomizableBean(Method m) {
		CustomizableBean beanAnnotation = m.getAnnotation(CustomizableBean.class);
		if (beanAnnotation != null) {
			BeanIdentification beanIdentification = getBeanIdentification(beanAnnotation, m);
			customizableBeans.put(beanIdentification, beanIdentification);
		}
	}

	private void analyzeBean(Method m) {
		Bean beanAnnotation = m.getAnnotation(Bean.class);
		if (beanAnnotation != null) {
			BeanIdentification beanIdentification = getBeanIdentification(beanAnnotation, m);
			nonCustomizableBeans.put(beanIdentification, beanIdentification);
		}
	}

	private BeanIdentification getBeanIdentification(CustomizableBean beanAnnotation, Method m) {
		BeanIdentification bi = new BeanIdentification();
		bi.setBeanType(m.getReturnType().getName());
		bi.setBeanName(getBeanName(beanAnnotation, m));
		
		// TODO: Add @Qualifier handling to BeanIdentification.
		
		return bi;
	}

	private BeanIdentification getBeanIdentification(Bean beanAnnotation, Method m) {
		BeanIdentification bi = new BeanIdentification();
		bi.setBeanType(m.getReturnType().getName());
		bi.setBeanName(getBeanName(beanAnnotation, m));
		
		// TODO: Add @Qualifier handling to BeanIdentification.
		
		return bi;
	}

	private BeanIdentification getBeanIdentification(Bean beanAnnotation, ExecutableElement methodElement) {
		BeanIdentification bi = new BeanIdentification();
		bi.setBeanType(getBeanTypeName(methodElement));
		bi.setBeanName(getBeanName(beanAnnotation, methodElement));
		
		// TODO: Add @Qualifier handling to BeanIdentification.
		
		return bi;
	}

	private String getBeanTypeName(ExecutableElement methodElement) {
		TypeMirror beanType = typeUtils.erasure(methodElement.getReturnType());
		String beanTypeName = beanType.toString();
		return beanTypeName;
	}

	private String getBeanName(CustomizableBean beanAnnotation, Method m) {
		if (beanAnnotation.name() != null && beanAnnotation.name().length > 0) {
			return beanAnnotation.name()[0];
		}
		return m.getName();
	}
	
	private String getBeanName(Bean beanAnnotation, Method m) {
		if (beanAnnotation.name() != null && beanAnnotation.name().length > 0) {
			return beanAnnotation.name()[0];
		}
		return m.getName();
	}

	private String getBeanName(Bean beanAnnotation, ExecutableElement methodElement) {
		if (beanAnnotation.name() != null && beanAnnotation.name().length > 0) {
			return beanAnnotation.name()[0];
		}
		return methodElement.getSimpleName().toString();
	}

}
