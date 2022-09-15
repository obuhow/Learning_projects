#ifndef SRC_CALCULATOR_H_
#define SRC_CALCULATOR_H_

#include "math.h"
#include "defines.h"
#include "stack.h"
#include "coord_struct.h"
#include "expression_parse.h"

void calculate_arr(char *expr, coord *arr);
double calculate(char *expr, double var);
void var_to_num(char *expr, int i, Double_stack* numbers, double var);
void dig_to_num(char *expr, int *i, Double_stack* numbers);
void perform_math_operations(char *expr, int i, Double_stack* numbers, int *stop);

#endif  // SRC_CALCULATOR_H_
