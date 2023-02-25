// Задача: реализовать работу алгоритма Ли.
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

//-------------------------------------------------------------------------
// создать класс Point который будет имитировать точку на дискретном поле
class Point {
    // координаты
    public int row;
    public int col;
    // конструктор по умолчанию
    public Point(){
        row = 0;
        col = 0;
    }
    // переггрузка конструктора класса
    public Point(int i, int j){
        row = i;
        col = j;
    }
    // метод установки координат для точки
    public void setPoint(int i, int j){
        row = i;
        col = j;
    }
    // метод для проверки свободна ли координата
    public boolean isFree(int[][] array){
        boolean answer = false;
        if (array[row][col] >= 0){
            answer = true;
        }
        return answer;
    }
}

//-------------------------------------------------------------------------
// основной класс
public class task6{
    public static void main(String[] args) throws IOException, FileNotFoundException{
        try {
            // считать исходное поле
            int[][] field = readField("field.txt");
            System.out.println("Исходная карта.");
            showField(field);
            // задать координату начала и конца пути
            Point startPoint = new Point(1, 9);
            Point endPoint = new Point(13, 6);
            if (startPoint.isFree(field) & endPoint.isFree(field)){
                // инициировать волну на поле, показать результат
                waveSpread(field, startPoint);
                //System.out.println("Карта с весовыми коэффициентами пути.");
                //showField(field);
                // найти путь от начальной до конечой точки, показать результат
                wayOut(field, startPoint, endPoint);
                System.out.println("Карта с путем выхода.");
                showField(field);
            }
            else {
                System.out.println("Начальная или конечная точка указаны неверно.");;
            }
        // обработка исключений
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    //-------------------------------------------------------------------------
    // метод поиска кратчайшего пути
    public static void wayOut(int[][] field, Point starPoint, Point endPoint){
        // добавить в очередь конечную точку и идти к ней
        Queue<Point> queue = new LinkedList<Point>();
        queue.add(endPoint);
        Point point = queue.element();
        // пока не достигнуто начало
        while (queue.isEmpty() == false){
            point = queue.element();
            // правило обхода
            if(field[point.row - 1][point.col] > 0){
                if(field[point.row - 1][point.col] < field[point.row][point.col]){
                    queue.add(new Point(point.row - 1, point.col));
                    field[point.row][point.col] = -2;
                }
            }
            if(field[point.row][point.col + 1] > 0){
                if(field[point.row][point.col + 1] < field[point.row][point.col]){
                    queue.add(new Point(point.row, point.col + 1));
                    field[point.row][point.col] = -2;
                }
            }
            if(field[point.row + 1][point.col] > 0){
                if(field[point.row + 1][point.col] < field[point.row][point.col]){
                    queue.add(new Point(point.row + 1, point.col));
                    field[point.row][point.col] = -2;
                }
            }
            if(field[point.row][point.col - 1] > 0){
                if(field[point.row][point.col - 1] < field[point.row][point.col]){
                    queue.add(new Point(point.row, point.col - 1));
                    field[point.row][point.col] = -2;
                }
            }
            queue.remove();
        }
        // отметить начало и конец пути
        field[starPoint.row][starPoint.col] = -3;
        field[endPoint.row][endPoint.col] = -3;
    }

    //-------------------------------------------------------------------------
    // метод распространения волны
    public static void waveSpread(int[][] field, Point startPoint){
        Queue<Point> queue = new LinkedList<Point>();
        // добавить в очередь начльную точку и идти от нее
        queue.add(startPoint);
        Point point = queue.element();
        field[point.row][point.col]++;
        while(queue.isEmpty() == false){
            point = queue.element();
            // правило распространения
            if(field[point.row - 1][point.col] != -1){
                if(field[point.row - 1][point.col] == 0){
                    queue.add(new Point(point.row - 1, point.col));
                    field[point.row - 1][point.col] = field[point.row][point.col] + 1;
                }
            }
            if(field[point.row][point.col + 1] != -1){
                if(field[point.row][point.col + 1] == 0){
                    queue.add(new Point(point.row, point.col + 1));
                    field[point.row][point.col + 1] = field[point.row][point.col] + 1;
                }
            }
            if(field[point.row + 1][point.col] != -1){
                if(field[point.row + 1][point.col] == 0){
                    queue.add(new Point(point.row + 1, point.col));
                    field[point.row + 1][point.col] = field[point.row][point.col] + 1;
                }
            }
            if(field[point.row][point.col - 1] != -1){
                if(field[point.row][point.col - 1] == 0){
                    queue.add(new Point(point.row, point.col - 1));
                    field[point.row][point.col - 1] = field[point.row][point.col] + 1;
                }
            }
            queue.remove();
        }
    }

    //-------------------------------------------------------------------------
    // метод для считывания поля из файла
    public static int[][] readField(String filePath) throws IOException, FileNotFoundException {
        File file = new File(filePath);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        ArrayList<String> tArr = new ArrayList<String>();
        String[] tmpArray = new String[1_000];
        int rows = 0;
        // считать 
        while ((line = reader.readLine()) != null){
            tmpArray = line.replaceAll(" ", "").split(",");
            for (int i = 0; i < tmpArray.length; i++){
                tArr.add(tmpArray[i]);
            }
            rows++;
        }
        reader.close();
        int cols = tArr.size()/rows;
        int[][] field = new int[rows][cols];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                field[i][j] = Integer.parseInt(tArr.get(0));
                tArr.remove(0);
            }
        }
        return field;
    }
    //-------------------------------------------------------------------------
    // переменные для окрашивания поля при выводе
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m"; 
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";   
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_RESET = "\u001B[0m";
    // метод вывода поля в консоль
    public static void showField(int[][] array){
        System.out.printf("%3s", " ");
        for (int i = 0; i < array[0].length; i++) {
            System.out.printf("%3d", i);
        }
        System.out.println();
        for (int i = 0; i < array.length; i++){
            System.out.printf("%3d", i);
            for (int j = 0; j < array[0].length; j++){
                if (array[i][j] == -1) System.out.printf(ANSI_RED_BACKGROUND + "%3s" + ANSI_RESET, " ");
                else if (array[i][j] == -2) System.out.printf(ANSI_BLUE_BACKGROUND + "%3s" + ANSI_RESET, " ");
                else if (array[i][j] == -3) System.out.printf(ANSI_GREEN_BACKGROUND + "%3s" + ANSI_RESET, ">.<");
                else System.out.printf(ANSI_WHITE_BACKGROUND + "%3s" + ANSI_RESET, " ");
            }
            System.out.println();
        }
    }
}