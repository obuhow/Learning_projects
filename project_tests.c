#include "take_string.h"
#include <stdio.h>
#include <string.h>

void convert_to_polish_notation_test();

int main() {
    convert_to_polish_notation_test();
    return 0;
}

void convert_to_polish_notation_test() {
    printf("Testing convert_to_polish_notation():\n\t");
    const int tests_cnt = 7;
    char *input[] = {"10+15", "5+x*2", "x*(5+21)/3",
         "(x*(x*(x*(x+2))))", "(sin(x*2))^2",
         "sin(sin(sin(sin(1/ln(x^123)))))", "sqrt(-(-x^(-2)))"};
         *expected_output[] = {"10 15 +", "5 x 2 * +",
          "x 5 21 + * 3 /", "x x x x 2 + * * *", "x 2 * sin 2 ^",
           "1 x 123 ^ ln / sin sin sin sin", "x 2 - ^ - - sqrt"};
    for (int i = 0; i < tests_cnt; i++) {
        printf("test %d:\n\t", i + 1);
        printf("input: %s\n\t", input[i]);
        char *res = convert_to_polish_notation(input[i], str_length(input[i]));
        printf("output: %s\n\t", res);
        printf("output fact: %s\n\t", expected_output[i]);
        printf(strcmp(res, expected_output[i]) == 0 ?
                "SUCCESS\n" : "FAIL\n");
    }
}
