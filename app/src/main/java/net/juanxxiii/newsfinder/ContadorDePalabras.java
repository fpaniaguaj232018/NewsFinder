package net.juanxxiii.newsfinder;

public class ContadorDePalabras {
    public static int contarPalabras(StringBuilder texto, String palabra){
        String textoMinusculas = texto.toString().toLowerCase();
        palabra = palabra.toLowerCase();
        int numeroVeces = 0;
        int pos=0;
        while(textoMinusculas.indexOf(palabra,pos)!=-1){
            pos = textoMinusculas.indexOf(palabra, pos)+palabra.length();
            numeroVeces++;
        }
        return numeroVeces;
    }
}
