package edu.cscc.jpaexercise.jpaexercise.models;

import java.util.List;
import java.util.Map;

public record ErrorDetail(String message, Map<String, List<ValidationError>> errors) {
}
