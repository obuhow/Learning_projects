#ifndef SRC_POLISH_NOTATION_H_
#define SRC_POLISH_NOTATION_H_

#include "defines.h"
#include "expression_parse.h"
#include "stack.h"

void pull_from_stack_to_input(Char_stack* stack, char* res, int* id_res);
void pull_remaining(Char_stack *stack, char *res, int *res_idx);
void pull_until_bracket(Char_stack* stack, char* res, int* res_idx);
void push_operator(Char_stack* stack, char op, int* idx, char op_past, char* res, int* res_idx);
int push_math_function(Char_stack* stack, char* str, int* idx, char* math_functions[]);
int read_double(char *str, int *idx, char *res, int *res_idx);
char* convert_to_polish_notation(char* str);
int process_symbol(Char_stack *stack, char *str, int *idx, char *res, int *res_idx);
void put_to_res(char c, char* res, int* id_res);

#endif  // SRC_POLISH_NOTATION_H_
