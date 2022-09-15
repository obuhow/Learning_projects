#include "stack.h"

Char_stack *init_char_stack() {
    Char_stack *stack = NULL;
    stack = malloc(sizeof(Char_stack));
    if (stack == NULL)
        stack = realloc(stack, sizeof(Char_stack));
    stack->data = malloc(stack->size * sizeof(char));
    if (stack->data == NULL)
        stack->data = realloc(stack->data, stack->size * sizeof(char));
    stack->size = INIT_STACK_SIZE;
    stack->top = 0;
    return stack;
}

void destroy_char_stack(Char_stack *stack) {
    if (stack->data != NULL)
        free(stack->data);
    if (stack != NULL)
        free(stack);
    stack = NULL;
}

int get_top_char(Char_stack *stack) {
    return stack->top;
}

void push_char_stack(Char_stack *stack, char value) {
    if (get_top_char(stack) >= stack->size)
        resize_char_stack(stack);
    stack->data[get_top_char(stack)] = value;
    stack->top++;
}


char pop_char_stack(Char_stack *stack) {
    get_top_char(stack) > 0 ? stack->top-- : printf("Stack Underflow");
    return stack->data[get_top_char(stack)];
}

char peek_char_stack(Char_stack *stack) {
    if (get_top_char(stack) <= 0)
        printf("Stack Underflow");
    return stack->data[get_top_char(stack) - 1];
}

void resize_char_stack(Char_stack *stack) {
    stack->size += INIT_STR_SIZE;
    stack->data = realloc(stack->data, stack->size * sizeof(char));
}

Double_stack *init_double_stack() {
    Double_stack *stack = NULL;
    stack = malloc(sizeof(Double_stack));
    if (stack == NULL)
        stack = realloc(stack, sizeof(Double_stack));
    stack->data = malloc(stack->size * sizeof(double));
    if (stack->data == NULL)
        stack->data = realloc(stack->data, stack->size * sizeof(double));
    stack->size = INIT_STACK_SIZE;
    stack->top = 0;
    return stack;
}

void destroy_double_stack(Double_stack *stack) {
    if (stack->data != NULL)
        free(stack->data);
    if (stack != NULL)
        free(stack);
    stack = NULL;
}

int get_top_double(Double_stack *stack) {
    return stack->top;
}

void push_double_stack(Double_stack *stack, double value) {
    if (get_top_double(stack) >= stack->size)
        resize_double_stack(stack);
    stack->data[get_top_double(stack)] = value;
    stack->top++;
}


double pop_double_stack(Double_stack *stack) {
    get_top_double(stack) > 0 ? stack->top-- : printf("Stack Underflow");
    return stack->data[get_top_double(stack)];
}

double peek_double_stack(Double_stack *stack) {
    if (get_top_double(stack) <= 0)
        printf("Stack Underflow");
    return stack->data[get_top_double(stack) - 1];
}

void resize_double_stack(Double_stack *stack) {
    stack->size += INIT_STACK_SIZE;
    stack->data = realloc(stack->data, stack->size * sizeof(double));
}
