#include "calculator.h"

void calculate_arr(char *expr, coord *arr) {
    double step = SCALE_X / (WIDTH - 1);
    arr[0].x = 0;
    for (int i = 0; i < WIDTH; i++) {
        if (i != WIDTH - 1) arr[i + 1].x = arr[i].x + step;
        arr[i].y = calculate(expr, arr[i].x);
    }
}

double calculate(char *expr, double var) {
    Double_stack* numbers = init_double_stack();
    int stop = 0;
    for (int i = 0; expr[i] && !stop; i++) {
        dig_to_num(expr, &i, numbers);
        var_to_num(expr, i, numbers, var);
        perform_math_operations(expr, i, numbers, &stop);
    }
    double res = numbers->data[numbers->top - 1];
    destroy_double_stack(numbers);
    return stop ? 1000 : res;
}

void var_to_num(char *expr, int i, Double_stack* numbers, double var) {
    if (expr[i] == X_1)
        push_double_stack(numbers, var);
}

void dig_to_num(char *expr, int *i, Double_stack* numbers) {
    int count = 0;
    char digits[100];
    double num;
    if (is_number(expr[*i])) {  // RTYUIOGHFDFJHGJSHFKL;FJHSJDKFLJHDSAHBDKLFFJDHSBAJDKLFJHDSADIODJSHDJHDSFIBVD
        while (is_number(expr[*i]) || expr[*i] == _POINT) {
            digits[count++] = expr[(*i)++];
        }
        digits[count] = '\0';
        num = atof(digits);
        push_double_stack(numbers, num);
        (*i)--;
    }
}

void perform_math_operations(char *expr, int i, Double_stack* numbers, int *stop) {
    if (expr[i] == PLUS)
        push_double_stack(numbers, pop_double_stack(numbers) + pop_double_stack(numbers));
    if (expr[i] == MINUS) {
        double second_num = pop_double_stack(numbers);
        double first_num = pop_double_stack(numbers);
        push_double_stack(numbers, first_num - second_num);
    }
    if (expr[i] == MULT)
        push_double_stack(numbers, pop_double_stack(numbers) * pop_double_stack(numbers));
    if (expr[i] == DIV) {
        double second_num = pop_double_stack(numbers);
        double first_num = pop_double_stack(numbers);
        second_num == 0 ? *stop = 1 : push_double_stack(numbers, first_num / second_num);
    }
    if (expr[i] == POW) {
        double degree = pop_double_stack(numbers);
        push_double_stack(numbers, pow(pop_double_stack(numbers), degree));
    }
    if (expr[i] == _SIN)
        push_double_stack(numbers, sin(pop_double_stack(numbers)));
    if (expr[i] == _COS)
        push_double_stack(numbers, cos(pop_double_stack(numbers)));
    if (expr[i] == _TANG) {
        double num = pop_double_stack(numbers);
        push_double_stack(numbers, sin(num) / cos(num));
    }
    if (expr[i] == _CTANG) {
        double num = pop_double_stack(numbers);
        push_double_stack(numbers, cos(num) / sin(num));
    }

    if (expr[i] == _SQRT) {
        double num = pop_double_stack(numbers);
        num < 0 ? *stop = 1 : push_double_stack(numbers, sqrt(num));
    }
    if (expr[i] == _LN) {
        double num = pop_double_stack(numbers);
        num < 0 ? *stop = 1 : push_double_stack(numbers, log(num));
    }
    if (expr[i] == UNAR_MINUS)
        push_double_stack(numbers, - pop_double_stack(numbers));
}
