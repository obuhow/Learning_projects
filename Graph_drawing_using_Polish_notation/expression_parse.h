#ifndef SRC_EXPRESSION_PARSE_H_
#define SRC_EXPRESSION_PARSE_H_

#include "defines.h"

int is_operator(char op);
int is_math_function(char op);
int is_unar_minus(char op, int* idx, char op_past);
int is_number(char num);
char get_math_function_number(int i);
int str_length(char* str);
int is_correct_function(char *str, int idx, char *math_function);
int priority(char op);
int error_status(char *str);

#endif  // SRC_EXPRESSION_PARSE_H_
