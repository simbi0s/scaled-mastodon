package org.test.mastodon.postgateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MastodonAccount {

	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		MastodonAccount that = (MastodonAccount) o;

		return username.equals(that.username);
	}

	@Override
	public int hashCode() {
		return username.hashCode();
	}
}
