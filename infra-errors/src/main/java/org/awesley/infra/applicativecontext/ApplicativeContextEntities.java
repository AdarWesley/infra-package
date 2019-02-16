package org.awesley.infra.applicativecontext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public class ApplicativeContextEntities {
	
	private static ThreadLocal<LinkedHashMap<String, ArrayList<ContextEntityInfo>>> contextEntities = 
			new ThreadLocal<LinkedHashMap<String, ArrayList<ContextEntityInfo>>>();

	public static long count() {
		LinkedHashMap<String, ArrayList<ContextEntityInfo>> map = contextEntities.get();
		if (map == null) {
			return 0L;
		}
		return map.size();
	}

	public static void addContextEntities(String contextKey, ContextEntityInfo contextEntityInfo) {
		ArrayList<ContextEntityInfo> contextEntityList = verifyInitializedAndGetEntityList(contextKey);
		contextEntityList.add(contextEntityInfo);
	}

	private static ArrayList<ContextEntityInfo> verifyInitializedAndGetEntityList(String contextKey) {
		LinkedHashMap<String, ArrayList<ContextEntityInfo>> map = contextEntities.get();
		if (map == null) {
			map = new LinkedHashMap<String, ArrayList<ContextEntityInfo>>();
			contextEntities.set(map);
		}
		ArrayList<ContextEntityInfo> contextEntityList = map.get(contextKey);
		if (contextEntityList == null) {
			contextEntityList = new ArrayList<ContextEntityInfo>();
			map.put(contextKey, contextEntityList);
		}
		return contextEntityList;
	}

	public static void clear() {
		contextEntities.set(null);
	}

	public static Collection<ArrayList<ContextEntityInfo>> getContextEntities() {
		return contextEntities.get() != null? contextEntities.get().values() : null;
	}
}
