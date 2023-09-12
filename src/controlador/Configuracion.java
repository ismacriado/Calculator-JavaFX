package controlador;
public class Configuracion {

    private String propiedad;
    private String valor;

    public Configuracion(String propiedad, String valor) {
        this.propiedad = propiedad;
        this.valor = valor;
    }

    public String getPropiedad() {
        return propiedad;
    }

    public String getValor() {
        return valor;
    }
}

