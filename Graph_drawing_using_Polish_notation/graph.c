#include "stack.h"
#include "graph_drawing.h"
#include "calculator.h"
#include "polish_notation.h"
#include "defines.h"

void read_input(char *str);

int main() {
    char *expr = malloc(INIT_STR_SIZE * sizeof(char));
    read_input(expr);
    char *polish = convert_to_polish_notation(expr);
    if (!error_status(polish)) {
        coord coord_arr[WIDTH];
        calculate_arr(polish, coord_arr);
        draw_graph(coord_arr);
    }
    free(expr);
    free(polish);
    return 0;
}

void read_input(char* str) {
    int idx = 0, str_size = INIT_STR_SIZE;
    char c;
    while ((c = getchar()) != '\n') {
        if (c != SPACE)
            str[idx++] = c;
        if (idx == str_size) {
            str = realloc(str, str_size + INIT_STR_SIZE);
            str_size += INIT_STR_SIZE;
        }
    }
}
