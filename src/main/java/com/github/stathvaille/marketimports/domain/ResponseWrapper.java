package com.github.stathvaille.marketimports.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

/**
 *  Data should always be returned with an object on the outside. This is for security reasons.
 *  see https://www.owasp.org/index.php/OWASP_AJAX_Security_Guidelines#Always_return_JSON_with_an_Object_on_the_outside
 */
@Value
@AllArgsConstructor
public class ResponseWrapper<T> {
    private T data;
}
