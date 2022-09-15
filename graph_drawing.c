#include "graph_drawing.h"

void draw_graph(coord *arr) {
    for (int i = 0; i < HEIGHT; i++) {
        for (int j = 0; j < WIDTH; j++) {
            i == round(arr[j].y * (HEIGHT / 2)) + HEIGHT / 2 ?
            putchar(_LINE) : putchar(_POINT);
        }
        if (i != HEIGHT - 1) putchar('\n');
    }
}
