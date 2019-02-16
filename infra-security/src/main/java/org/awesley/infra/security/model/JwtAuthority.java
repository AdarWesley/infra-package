package org.awesley.infra.security.model;

public class JwtAuthority implements Authority {
	Long id;
	String name;

	public JwtAuthority(String name) {
		super();
		this.name = name;
	}
	
	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}
