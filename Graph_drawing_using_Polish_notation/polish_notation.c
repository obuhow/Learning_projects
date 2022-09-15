#include "polish_notation.h"

void pull_from_stack_to_input(Char_stack* stack, char* res, int* res_idx) {
    put_to_res(pop_char_stack(stack), res, res_idx);
    put_to_res(SPACE, res, res_idx);
}

void pull_remaining(Char_stack *stack, char *res, int *res_idx) {
    while (get_top_char(stack))
        pull_from_stack_to_input(stack, res, res_idx);
}

void pull_until_bracket(Char_stack* stack, char* res, int* res_idx) {
    while (peek_char_stack(stack) != BRACKET_OPEN)
        pull_from_stack_to_input(stack, res, res_idx);
    pop_char_stack(stack);
}

void push_operator(Char_stack* stack, char op, int* idx, char op_past,
                   char* res, int* res_idx) {
    op = (is_unar_minus(op, idx, op_past) ? UNAR_MINUS : op);
    while (get_top_char(stack) && op != UNAR_MINUS &&
           (priority(op) <= priority(peek_char_stack(stack))))
        pull_from_stack_to_input(stack, res, res_idx);
    push_char_stack(stack, op);
}

int push_math_function(Char_stack* stack, char* str, int* idx, char* math_functions[]) {
    int flag = 1;
    for (int i = 0; i < MATH_FUNCTIONS_CNT; i++) {
        if (is_correct_function(str, *idx, math_functions[i])) {
            push_char_stack(stack, get_math_function_number(i));
            *idx += str_length(math_functions[i]) - 2;
        }
    }
    return flag;
}

int read_double(char *str, int *idx, char *res, int *res_idx) {
    int return_value = 1, points = 0;
    while ((is_number(str[*idx]) && str[*idx] != '\0') || str[*idx] == _POINT) {
        if (str[*idx] == _POINT) points++;
        put_to_res(str[*idx], res, res_idx);
        (*idx)++;
    }
    if (points > 1) return_value = 0;
    (*idx)--;
    put_to_res(SPACE, res, res_idx);
    return return_value;
}

char* convert_to_polish_notation(char* str) {
    char* res = malloc(2 * INIT_STR_SIZE * sizeof(char));
    int res_idx = 0, idx = 0;
    Char_stack* stack = init_char_stack();
    while (str[idx] != '\0' && !error_status(res)) {
        process_symbol(stack, str, &idx, res, &res_idx);
    }
    if (!error_status(res))
        pull_remaining(stack, res, &res_idx);
    res[res_idx + 1] = '\0';
    destroy_char_stack(stack);
    return res;
}

int error_status(char *str) {
    return (str[0] == 'E' ? 1 : 0);
}

int process_symbol(Char_stack *stack, char *str, int *idx, char *res, int *res_idx) {
    int correct_status = 1;
    char *math_functions[] = {"sin(", "cos(", "tg(", "ctg(", "ln(", "sqrt("};
    if (is_number(str[*idx])) {
        correct_status = read_double(str, idx, res, res_idx);
    } else if (str[*idx] == X_1 || str[*idx] == X_2) {
        put_to_res(str[*idx], res, res_idx);
        put_to_res(SPACE, res, res_idx);
    } else if (str[*idx] == BRACKET_OPEN) {
        push_char_stack(stack, str[*idx]);
    } else if (str[*idx] == BRACKET_CLOSE) {
        pull_until_bracket(stack, res, res_idx);
    } else if (is_operator(str[*idx])) {
        push_operator(stack, str[*idx], idx, str[*idx - 1], res, res_idx);
    } else if (is_math_function(str[*idx])) {
        correct_status = push_math_function(stack, str, idx, math_functions);
    } else {
        correct_status = 0;
    }
    if (!correct_status) {
        res[0] = 'E';
        correct_status = 0;
    }
    (*idx)++;
    return correct_status;
}

void put_to_res(char c, char* res, int* res_idx) {
    res[*res_idx] = c;
    (*res_idx)++;
}
