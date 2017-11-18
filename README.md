# EDA FlightSystem

- Descomprimir el proyecto.

- Abrir la carpeta del proyecto en el IDE IntelliJ preferentemente, sino en Eclipse.

- Ir a la barra de herramientas, buscar la opción **Build**, clickear en **Build artifacts** y **Rebuild**.

- Para ejecutar el **.jar**, ir a la carpeta donde se creó el mismo (mediante la consola) y poner ```java -jar eda-flightsystem.jar```

- En caso de tener problemas con la [librería de KML Micromata](https://labs.micromata.de/projects/jak/faq.html) consultar su guía.

### Archivos de entrada y salida

- En caso de ejecutar el proyecto en una IDE entonces deberá especificar el *path* absoluto al querer leer desde un archivo o guardar la salida.
    * Por ejemplo, si se quiere añadir todos los vuelos desde archivo **Flights.txt** ubicado en la carpeta **/src/Data/Input** entonces se deberá ingresar el comando ```insert all flights /src/Data/Input/Flights.txt replace```

- En caso de ejecutar el proyecto desde el **.jar** entonces los archivos deberán ser ubicados en la misma carpeta que esté ubicado el **.jar** o ingresar nuevamente el *path* completo. 