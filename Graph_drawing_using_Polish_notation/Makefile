CFLAGS = -Wall -Wextra -Werror
DIR = .

all: graph

graph: graph.o calculator.o expression_parse.o graph_drawing.o polish_notation.o stack.a
	gcc $(CFLAGS) $(DIR)/graph_drawing.o $(DIR)/polish_notation.o $(DIR)/expression_parse.o $(DIR)/calculator.o $(DIR)/graph.o -L$(DIR) -lstack -o $(DIR)/graph
	rm -rf $(DIR)/*.o

graph.o: graph.c
	gcc $(CFLAGS) -c graph.c -o $(DIR)/graph.o

calculator.o: calculator.c
	gcc $(CFLAGS) -c calculator.c -o $(DIR)/calculator.o

expression_parse.o: expression_parse.c
	gcc $(CFLAGS) -c expression_parse.c -o $(DIR)/expression_parse.o

graph_drawing.o: graph_drawing.c
	gcc $(CFLAGS) -c graph_drawing.c -o $(DIR)/graph_drawing.o

polish_notation.o: polish_notation.c
	gcc $(CFLAGS) -c polish_notation.c -o $(DIR)/polish_notation.o

stack.o: stack.c
	gcc $(CFLAGS) -c stack.c -o $(DIR)/stack.o

stack.a: stack.o
	ar rc $(DIR)/libstack.a $(DIR)/stack.o
	ranlib $(DIR)/libstack.a

clean:
	rm -rf $(DIR)/*

rebuild: clean all
