package org.test.mastodon.postgateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MastodonPost {

	private String id;
	private String content;
	private MastodonAccount account;

	@JsonProperty("created_at")
	private String createdAt;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MastodonAccount getAccount() {
		return account;
	}

	public void setAccount(MastodonAccount account) {
		this.account = account;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		MastodonPost that = (MastodonPost) o;

		if (!id.equals(that.id))
			return false;
		if (!content.equals(that.content))
			return false;
		if (!account.equals(that.account))
			return false;
		return createdAt.equals(that.createdAt);
	}

	@Override
	public int hashCode() {
		int result = id.hashCode();
		result = 31 * result + content.hashCode();
		result = 31 * result + account.hashCode();
		result = 31 * result + createdAt.hashCode();
		return result;
	}
}

