package controlador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import org.json.JSONObject;

public class Operaciones {

    HashMap<String, Double> unidadesTiempo = new HashMap<>();

    public Operaciones() {
        unidadesTiempo.put("milisegundos", 1.0);
        unidadesTiempo.put("segundos", 1000.0);
        unidadesTiempo.put("minutos", 60000.0);
        unidadesTiempo.put("horas", 3600000.0);
        unidadesTiempo.put("dias", 86400000.0);
        unidadesTiempo.put("semanas", 604800000.0);
        unidadesTiempo.put("años", 31557600000.0);
    }

    //limpia el contenido del historial.txt
    public static void clearFichero(File file){
         try{
            FileWriter writer = new FileWriter(file);
            writer.write("");
            
            writer.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    
    //borra una linea en concreto del fichero historial.txt
    public static void borrarLinea(int linea,File file){
        try{
            ArrayList <String> arr = new ArrayList<>();
            BufferedReader lector = new BufferedReader(new FileReader(file));
            BufferedWriter escritor = new BufferedWriter(new FileWriter(file,true));
            
            String lineaActual;
            int cont=0;
            while((lineaActual = lector.readLine())!=null){
                cont++;
                if(cont != linea){
                    arr.add(lineaActual+"\n");
                }
            } 
            FileWriter writer = new FileWriter(file);
            writer.write("");
            
            for (int i = 0; i < arr.size(); i++) {
                escritor.write(arr.get(i));
            }
            
            lector.close();
            escritor.close();
            writer.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    //Para comprobar que lo que entra es un número
    public static boolean isNumeric(String c) {
        try {
            int num = Integer.parseInt(c);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //Escribe en el fichero .txt historial los resultados y cuentas de las operaciones que ahce la calculadora
    public static void escribirFichero(String contenido, File file) {
        BufferedWriter br = null;
        try {
            br = new BufferedWriter(new FileWriter(file, true));
            br.write(contenido);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //Calcula el tiempo, recoge los datos del HashMap guardado en el constructor
    public String calcularTiempo(String valor, String origen, String destino) {
        Double valorEntrada = Double.valueOf(valor);
        Double factorOrigen = unidadesTiempo.get(origen);
        Double factorDestino = unidadesTiempo.get(destino);

        // Convertir la cantidad a milisegundos y luego a la unidad de destino
        return String.valueOf((valorEntrada * factorOrigen) / factorDestino);
    }

    //realiza la transformación de la divisa
    public static double realizarCalculoDivisa(double divisa_unitaria_origen, double divisa_unitaria_destino, double total_divisa_origen) {
        return total_divisa_origen * divisa_unitaria_destino / divisa_unitaria_origen;
    }

    // Se le pasa por parámetros el Json(str) y el prefijo que queremos coger, lo recupera y extrae su valor.
    public static double divisa(String str, String prefix) {
        JSONObject obj = new JSONObject(str);
        String result = obj.getJSONObject("conversion_rates").optString(prefix);
        double out = Double.parseDouble(result);
        return out;
    }

    //Esta función hace la request a la api de exchangerate, retorna un String con el JSON 
    public static String requestAPI(String prefix) {
        String enlace = "https://v6.exchangerate-api.com/v6/XXXX/latest/";
        String response = "";
        try {
            URL url = new URL(enlace + prefix);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int code = conn.getResponseCode();

            if (code != 200) {
                System.out.println("Request no exitosa");
            } else {
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    response += sc.nextLine();
                }
                sc.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return response;
    }

    //Para dar formato a la salida, si es decimal, lo devuelve como tal, en caso contrario, devolverá el número sin decimales
    public static String parsearDoubleToInt(double a) {
        String str = a + "";
        int cont = 0;
        String[] arr = str.split("\\.");
        for (int i = 0; i < arr[1].length(); i++) {
            if (arr[1].charAt(i) != '0') {
                cont++;
            }
        }
        int indice = str.indexOf(".");
        String out = str.substring(0, indice);
        if (cont > 0) {
            return str;
        } else {
            return out;
        }
    }

    public static double evaluarExpresion(String expresion) {
        //Una pila para operadores y otra para operandos
        Stack<Double> operandos = new Stack<>();
        Stack<Character> operadores = new Stack<>();
        boolean punto = false; // variable para indicar si se está leyendo un número decimal
        double numero = 0;

        //Recorremdos la expresión caracter por caracter
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            //Si el caracter es un dígito, lo agregamos al número actual
            if (Character.isDigit(c)) {
                if (punto) {
                    numero += (c - '0') / 10.0;
                } else {
                    numero = numero * 10 + (c - '0');
                }
                //Si el caracter es un punto, se marca que se está leyendo un número decimal
            } else if (c == '.') {
                punto = true;
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
                operandos.push(numero);
                numero = 0;
                punto = false;
                while (!operadores.empty() && prioridad(operadores.peek()) >= prioridad(c)) {
                    aplicarOperador(operandos, operadores);
                }
                operadores.push(c);
                // Si el caracter es un paréntesis de apertura, lo agregamos a la pila de operadores
            } else if (c == '(') {
                operadores.push(c);
                // Si el caracter es un paréntesis de cierre, aplicamos todos los operadores hasta encontrar el paréntesis de apertura correspondiente
            } else if (c == ')') {
                while (operadores.peek() != '(') {
                    aplicarOperador(operandos, operadores);
                }
                operadores.pop(); // sacar el paréntesis de apertura
            } else if (Character.isLetter(c)) {
                // leer una función trigonométrica
                String funcion = "";
                while (Character.isLetter(c)) {
                    funcion += c;
                    i++;
                    if (i < expresion.length()) {
                        c = expresion.charAt(i);
                    } else {
                        break;
                    }
                }
                i--; // retroceder una posición para leer el siguiente carácter correctamente
                switch (funcion) {
                    case "sin" ->
                        operadores.push('s');
                    case "cos" ->
                        operadores.push('c');
                    case "tan" ->
                        operadores.push('t');
                    default ->
                        throw new IllegalArgumentException("Función inválida: " + funcion);
                }
            }
        }

        operandos.push(numero); // agregar el último número
        while (!operadores.empty()) {
            aplicarOperador(operandos, operadores);
        }

        if (operandos.size() != 1 || !operadores.empty()) {
            throw new IllegalArgumentException("Expresión inválida: " + expresion);
        }

        return operandos.pop();
    }

    //indico la prioridad de la operación que me entra
    private static int prioridad(char c) {
        switch (c) {
            case '+', '-' -> {
                return 1;
            }
            case '*', '/' -> {
                return 2;
            }
            case '^', 's', 'c', 't' -> {
                return 3;
            }
            default ->
                throw new IllegalArgumentException("Operador o función inválida: " + c);
        }
    }

    private static void aplicarOperador(Stack<Double> operandos, Stack<Character> operadores) {
        //Saco de la pila el operador y los operandos a y b para hacer los cálculos
        char operador = operadores.pop();
        double b = operandos.pop();
        double a;
        double result = 0;
        switch (operador) {
            case '+' -> {
                a = operandos.pop();
                result = a + b;
            }
            case '-' -> {
                a = operandos.pop();
                result = a - b;
            }
            case '*' -> {
                a = operandos.pop();
                result = a * b;
            }
            case '/' -> {
                a = operandos.pop();
                if (b == 0) {
                    throw new ArithmeticException("División por cero");
                }
                result = a / b;
            }
            case '^' -> {
                a = operandos.pop();
                result = Math.pow(a, b);
            }
            case 's' ->
                result = Math.sin(b);
            case 'c' ->
                result = Math.cos(b);
            case 't' ->
                result = Math.tan(b);
            default ->
                throw new IllegalArgumentException("Operador inválido: " + operador);
        }
        operandos.push(result);
    }
}
