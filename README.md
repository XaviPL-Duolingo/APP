# BUHOLINGO | APP
![MainFrame](https://i.ibb.co/VvzBpJx/Buholingo-Portada.jpg)
Repositorio de la parte "CLIENTE" del proyecto Buholingo. <br>
Aplicación para plataformas ANDROID desde donde el usuario puede aprender idiomas mientras hace diferentes ejercicios hasta 12 diferentes IDIOMAS para escoger y un máximo de 132 CURSOS (actualmente implementados, pueden ser más), variedad de CATEGORIAS por curso y diferentes NIVELES de dificultad por categoría. Implementados actualmente 7 TIPOS de EJERCICIOS con sus diferentes LAYOUTS y puntuaciones.

## SPECS:
- Conexión con SERVER DESKTOP mediante ServerSocket
- Patrón de diseño DAO
- Implementación de lectura y escritura de datos con JSON
- Encriptación de datos mediante MD5
- Uso de RecyclerView y Spinners con CustomAdapters


## FUNCIONALIDADES:
- Libre elección de idioma base del usuario (12 impl.)
- Múltiples cursos con disitintos idiomas por idioma base
- Implementación de LOGIN + REGISTRO con capacidad para compartir progreso en diferentes dispositivos

- Iconos dinámicos por CATEGORÍA e implementación de GridLayout alternado.
    - Nombre de la CATEGORÍA
    - ProgressBar con el progreso del usuario en cada categoría
    - Indicador del NIVEL por donde el usuario va
    
- Variedad de TIPOS DE EJERCICIOS con diferentes Layouts
    - TRAD. ABIERTA: El usuario debe traducir una frase escribiendola a mano, pueden haber multiples respuestas.
    - REORDENAR: El usuario debe traducir una frase con las palabras en el otro idioma en el orden que toca.
    - COMPLETAR FRASE: El usuario debe completar la frase seleccionando la palabra correcta entre las multiples opciones.
    - EMPAREJAR: El usuario debe traducir una lista de palabras.
    - TIPO TEST: El usuario debe elegir entre las multiples opciones la opción con la traduccion correcta.
    - VARIANTE TRAD. ABIERTA: Incluye TTS (TextToSpeech)
    - VARIANTE REORDENAR: Incluye TTS (TextToSpeech)
  
- Liga y clasificación de usuarios mediante su ELO (equivale a su puntuación total)
- Diferentes ligas y rangos, con diferentes puntuaciones de corte (EX: DIAMOND: 20000 ELO)
- Implementación de TIENDA con ITEMS y VENTAJAS
- Modo OFFLINE y modo SIN CONEXION.


## IMAGENES:
![MainActivity](https://i.ibb.co/w7bqJpJ/Buholingo-Main.png)
![Ejercicios](https://i.ibb.co/0B0BBgM/Ejercicios.png)
![LoginRegistro](https://i.ibb.co/k8z27cw/Login-Registro.png)
![Perfil](https://i.ibb.co/hMpbsn6/Perfil.png)
![Ranking](https://i.ibb.co/s3cBb4w/Ranking.png)
