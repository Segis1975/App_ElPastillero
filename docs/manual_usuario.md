# Manual de usuario

## Introducción

**El Pastillero** es una aplicación Android para organizar la medicación diaria. Permite
dar de alta medicamentos, controlar el stock, registrar las tomas, anotar las citas
médicas y mantener una lista personal distinta para cada usuario.

Este manual explica cómo usar la aplicación con la cuenta de demostración, pantalla por
pantalla. Los datos que se describen se cargan solos la primera vez que se abre la app,
así que se puede seguir el recorrido completo sin crear nada previamente.

## 1. Iniciar sesión

Al abrir la aplicación aparece la pantalla de acceso.

1. Escribe el correo `sergio@pastillero.es` y la contraseña `1234`.
   También puedes pulsar el texto gris de la parte inferior para que se rellenen solos.
2. Pulsa **Entrar**.

Si marcas **Recordarme**, en los siguientes arranques la aplicación entrará directamente.

### Crear una cuenta nueva

1. Pulsa **«¿No tienes cuenta? Crear una»**.
2. Rellena nombre, correo y contraseña (mínimo 4 caracteres).
3. Pulsa **Crear cuenta**. La nueva cuenta empieza sin medicamentos en su lista personal.

## 2. Pantalla de inicio

Es el resumen del día. Muestra:

- **Stock general**: barra de progreso con la suma de unidades de todos los medicamentos.
- **Medicamentos**: número total.
- **Con stock bajo**: medicamentos en el mínimo o por debajo.
- **Tomas pendientes**: tomas todavía no marcadas.

Desde aquí puedes usar el menú lateral (☰) o los tres botones inferiores para ir a
Medicamentos, Tomas o Citas.

## 3. Medicamentos

### Ver el listado

En el menú lateral elige **Medicamentos**. Cada medicamento aparece en una tarjeta con su
nombre, principio activo, dosis, valoración en estrellas y stock. Si el stock está bajo
aparece un icono de aviso rojo.

### Buscar y ordenar

- Toca la **lupa** y escribe parte del nombre o del principio activo.
- Toca el icono de **ordenar** y elige: por nombre, por stock o por valoración.

### Añadir un medicamento

1. Pulsa el botón **+** (o el menú **Añadir**).
2. Rellena los campos:
   - **Nombre** (obligatorio).
   - Principio activo, dosis, stock, stock mínimo y frecuencia en horas.
   - **Unidades por toma** con la barra deslizante.
   - **Tipo** y **presentación**: al cambiar el tipo, las presentaciones cambian solas.
   - **Estado**: activo, pausado o finalizado.
   - **Recordatorio** y **Favorito** con los interruptores.
   - **Valoración** con las estrellas.
   - **Fecha de inicio**: se elige con el selector de fecha y hora.
3. Pulsa **Guardar**.

### Editar o borrar

- Desde el listado, mantén pulsada una tarjeta para abrir el menú contextual con
  **Editar** y **Eliminar**.
- Desde la ficha del medicamento puedes usar los botones **Editar** y **Eliminar**, o el
  menú de la barra superior.
- Al eliminar se pide confirmación; se borran también todas las tomas asociadas.

## 4. Ficha del medicamento

Muestra todos los datos, una barra con el stock actual y la lista de tomas registradas.

- **Añadir a mi lista personal**: activa el interruptor para que el medicamento aparezca
  en tu lista personal.
- **Registrar toma**: elige la fecha y la hora y se creará una toma pendiente.
- En cada toma, la casilla indica si está tomada. Al marcarla se descuenta el stock; al
  desmarcarla se devuelve.
- Mantén pulsada una toma para **marcarla como tomada** o **eliminarla**.

## 5. Control de tomas

En el menú lateral elige **Control de tomas**.

- Se listan todas las tomas de todos los medicamentos, con el nombre del medicamento, la
  fecha, la cantidad y el estado.
- Marca la casilla de una toma para registrarla (descuenta stock).
- Pulsa **+** para elegir un medicamento y registrar una toma nueva con fecha y hora.
- Mantén pulsada una toma para abrir el menú contextual.

## 6. Citas médicas

En el menú lateral elige **Citas médicas**.

- Pulsa **+** para crear una cita: especialidad, médico, lugar, fecha y hora, y notas.
- Toca una cita para editarla.
- Mantén pulsada una cita para editarla o eliminarla desde el menú contextual.
- La fecha y la hora se eligen con un selector propio que combina calendario y reloj.

## 7. Mi lista personal

En el menú lateral elige **Mi lista personal**.

- Pulsa **+** y elige un medicamento para añadirlo a tu lista.
- Toca un elemento para abrir su ficha completa.
- Mantén pulsado un elemento para **editar la nota** personal o **quitarlo** de la lista.

Cada usuario tiene su propia lista: si entras con `ana@pastillero.es` verás la lista vacía
aunque Sergio tenga elementos.

## 8. Ajustes

En el menú lateral elige **Ajustes**.

- **Tamaño de letra**: mueve la barra y suelta; la pantalla se redibuja con el nuevo
  tamaño. El cambio se aplica a toda la aplicación.
- **Avisos de recordatorio**: activa o desactiva los avisos internos.
- **Tema**: sistema, claro u oscuro.
- **Mi perfil**: cambia tu nombre y tu correo.
- **Cerrar sesión**: vuelve a la pantalla de acceso.
- **Borrar mi cuenta**: elimina la cuenta junto con su lista personal y sus citas. Pide
  confirmación y no se puede deshacer.

## 9. Acerca de

Muestra la versión de la aplicación, el autor, las tecnologías usadas y un enlace al
repositorio del proyecto.

## 10. Accesibilidad

- El tamaño de letra se puede aumentar hasta un 140 % desde Ajustes.
- Todos los iconos tienen descripción para los lectores de pantalla.
- Los colores mantienen contraste alto en tema claro y oscuro.
- Los controles son grandes y las tarjetas se pueden pulsar en toda su superficie.

## 11. Capturas de pantalla

En la carpeta `docs/capturas/` están las imágenes de cada pantalla tomadas en un emulador
con una instalación limpia: acceso, inicio, listado, ficha, formulario, tomas, citas, lista
personal, ajustes, acerca de, el diálogo de fecha y hora y una captura de la interfaz en
inglés. El anexo final de este manual las reproduce a tamaño de página.

## 12. Bibliografía y webgrafía

- Documentación oficial de Android Developers: <https://developer.android.com/docs>
- Guía de Material Design 3: <https://m3.material.io/>
- Guía de accesibilidad de Android: <https://developer.android.com/guide/topics/ui/accessibility>
- Apuntes y prácticas de la asignatura PMDM (IES Claudio Moyano, curso 2026-27).

## 13. Herramientas de IA utilizadas

Este manual y la revisión final del proyecto se han apoyado en un agente de IA (OpenCode).
El agente se usó para comprobar el cumplimiento del enunciado, detectar errores de estado al
girar la pantalla y redactar la documentación a partir del código real. Las decisiones de
diseño, los datos y las pruebas funcionales en el emulador se han revisado manualmente.
