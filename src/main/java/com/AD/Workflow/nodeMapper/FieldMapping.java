package com.AD.Workflow.nodeMapper;

import lombok.Data;

@Data
public class FieldMapping {

    /**
     * Target field in the destination node's input.
     * Example: /customer/finalAmount
     */
    private String targetPath;

    /**
     * Expression tree used to calculate the target value.
     */
    private String expression;
}