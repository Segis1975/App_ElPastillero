package es.ies.claudiomoyano.dam2.elpastillero;

public class Funciones {

    public String sacarProvincia (String cp){

        String[] provincias = {
                "Álava", "Albacete", "Alicante", "Almería", "Ávila",
                "Badajoz", "Baleares", "Barcelona", "Burgos", "Cáceres",
                "Cádiz", "Castellón", "Ciudad Real", "Córdoba", "A Coruña",
                "Cuenca", "Girona", "Granada", "Guadalajara", "Guipúzcoa",
                "Huelva", "Huesca", "Jaén", "León", "Lleida",
                "La Rioja", "Lugo", "Madrid", "Málaga", "Murcia",
                "Navarra", "Ourense", "Asturias", "Palencia", "Las Palmas",
                "Pontevedra", "Salamanca", "Santa Cruz de Tenerife", "Cantabria",
                "Segovia", "Sevilla", "Soria", "Tarragona", "Teruel",
                "Toledo", "Valencia", "Valladolid", "Vizcaya", "Zamora",
                "Zaragoza", "Ceuta", "Melilla"};
        int n = (Integer.parseInt(cp) / 1000)-1;
        return provincias[n];
    }
}
