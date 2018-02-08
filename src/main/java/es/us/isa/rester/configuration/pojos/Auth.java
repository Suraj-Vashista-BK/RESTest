
package es.us.isa.rester.configuration.pojos;

import java.util.List;

public class Auth {

    private Boolean required;
    private List<QueryParam> queryParams = null;
    private List<HeaderParam> headerParams = null;

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public List<QueryParam> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(List<QueryParam> queryParams) {
        this.queryParams = queryParams;
    }

    public List<HeaderParam> getHeaderParams() {
        return headerParams;
    }

    public void setHeaderParams(List<HeaderParam> headerParams) {
        this.headerParams = headerParams;
    }

}