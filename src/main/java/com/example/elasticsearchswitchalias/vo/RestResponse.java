package com.example.elasticsearchswitchalias.vo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class RestResponse {
	private String response;
	private int statusCode;
	private HttpStatus httpStatusCode;
	private String statusMessage;
	private String status;
	private String url;
    private HttpHeaders httpHeaders;


	public HttpStatus getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(HttpStatus httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    @Override
    public String toString() {
        return "RestResponse{" +
                "response='" + response + '\'' +
                ", statusCode=" + statusCode +
                ", statusMessage='" + statusMessage + '\'' +
                ", status='" + status + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
