package edu.cscc.securityexercises.models;

import java.util.List;
import java.util.Map;

public record ErrorDetail(String message, Map<String, List<ValidationError>> errors) {
}
