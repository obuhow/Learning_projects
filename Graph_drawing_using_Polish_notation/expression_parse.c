#include "expression_parse.h"

int is_operator(char op) {
    return (op == POW || op == PLUS || op == MINUS ||
            op == MULT || op == DIV);
}

int is_math_function(char op) {
    return (op == _SIN || op == _COS || op == _TANG ||
            op == _CTANG || op == _SQRT || op == _LN);
}

int is_unar_minus(char op, int* idx, char op_past) {
    return ((op == MINUS && *idx == 0) || (*idx > 0 &&
            (op_past == BRACKET_OPEN ||
             is_operator(op_past) ||
             is_math_function(op_past))));
}

int is_number(char num) {
    return (num >= ZERO && num <= NINE);
}

char get_math_function_number(int i) {
    char res;
    switch (i) {
        case 0: res = _SIN; break;
        case 1: res = _COS; break;
        case 2: res = _TANG; break;
        case 3: res = _CTANG; break;
        case 4: res = _LN; break;
        case 5: res = _SQRT; break;
    }
    return res;
}

int str_length(char* str) {
    int i = 0;
    while (str[i] != '\0') {
        i++;
    }
    return i;
}

int is_correct_function(char *str, int idx, char *math_function) {
    int math_function_len = str_length(math_function);
    int is_correct = 1;
    for (int j = 0; j < math_function_len; j++) {
        if (math_function[j] != str[idx + j]) {
            is_correct = 0;
        }
    }
    return is_correct;
}

int priority(char op) {
    int op_priority = -1;
    switch (op) {
        case PLUS: op_priority = 1; break;
        case MINUS: op_priority = 1; break;
        case UNAR_MINUS: op_priority = 1; break;
        case DIV: op_priority = 2; break;
        case MULT: op_priority = 2; break;
        case POW: op_priority = 3; break;
        case _SIN: op_priority = 4; break;
        case _COS: op_priority = 4; break;
        case _TANG: op_priority = 4; break;
        case _CTANG: op_priority = 4; break;
        case _LN: op_priority = 4; break;
        case _SQRT: op_priority = 4; break;
    }
    return op_priority;
}
