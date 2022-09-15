#ifndef SRC_STACK_H_
#define SRC_STACK_H_

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include "defines.h"

typedef struct Char_stack {
    char *data;
    int size;
    int top;
} Char_stack;

Char_stack *init_char_stack();
void destroy_char_stack(Char_stack *stack);
void push_char_stack(Char_stack *stack, char value);
char pop_char_stack(Char_stack *stack);
char peek_char_stack(Char_stack *stack);
void resize_char_stack(Char_stack *stack);
int get_top_char(Char_stack *stack);

typedef struct Double_stack {
    double *data;
    int size;
    int top;
} Double_stack;

Double_stack *init_double_stack();
void destroy_double_stack(Double_stack *stack);
void push_double_stack(Double_stack *stack, double value);
double pop_double_stack(Double_stack *stack);
double peek_double_stack(Double_stack *stack);
void resize_double_stack(Double_stack *stack);
int get_top_double(Double_stack *stack);

#endif  //  SRC_STACK_H_
