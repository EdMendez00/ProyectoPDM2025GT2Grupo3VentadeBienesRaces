# Manual de Instalación Completo
## Sistema de Venta de Bienes Raíces - Android Application

---

<p align="center">
  <img src="https://upload.wikimedia.org/wikipedia/commons/f/fa/Escudo_de_la_Universidad_de_El_Salvador.svg" width="200" alt="Logo UES FMOCC">
</p>

<p align="center">
<strong>UNIVERSIDAD DE EL SALVADOR</strong><br/>
<strong>FACULTAD DE INGENIERÍA Y ARQUITECTURA</strong><br/>
<strong>ESCUELA DE INGENIERÍA DE SISTEMAS INFORMÁTICOS</strong><br/>
</p>

<p align="center">
GRUPO TEÓRICO 02 - GRUPO DE PROYECTO 03<br/>
TUTOR: ING. CESAR AUGUSTO GONZALEZ RODRIGUEZ<br/>
MATERIA: PROGRAMACIÓN PARA DISPOSITIVOS MÓVILES (PDM115)
</p>

---

## 📋 Tabla de Contenidos

1. [Información General del Proyecto](#-información-general-del-proyecto)
2. [Requisitos del Sistema](#-requisitos-del-sistema)
3. [Configuración del Entorno de Desarrollo](#-configuración-del-entorno-de-desarrollo)
4. [Instalación de la Base de Datos](#-instalación-de-la-base-de-datos)
5. [Configuración de Firebase](#-configuración-de-firebase)
6. [Instalación del Código Fuente](#-instalación-del-código-fuente)
7. [Compilación y Generación del APK](#-compilación-y-generación-del-apk)
8. [Instalación de la Aplicación](#-instalación-de-la-aplicación)
9. [Configuración de Servicios Web](#-configuración-de-servicios-web)
10. [Pruebas de Funcionalidad](#-pruebas-de-funcionalidad)
11. [Datos de Prueba](#-datos-de-prueba)
12. [Información de Autoría](#-información-de-autoría)
13. [Solución de Problemas](#-solución-de-problemas)

---

## 🏠 Información General del Proyecto

### Descripción
Aplicación Android para la venta y búsqueda de bienes raíces que permite a los usuarios:
- Buscar y explorar propiedades disponibles
- Publicar propiedades en venta
- Programar visitas a propiedades
- Gestionar favoritos
- Administrar perfil de usuario

### Características Técnicas
- **Plataforma**: Android (API mínima 27)
- **Lenguaje**: Kotlin/Java
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Base de datos local**: Room Database
- **Base de datos remota**: Firebase Firestore
- **Autenticación**: Firebase Authentication
- **IDE recomendado**: Android Studio
- **Versión de compilación**: SDK 35

---

## 💻 Requisitos del Sistema

### Requisitos de Hardware
- **RAM**: Mínimo 8GB (recomendado 16GB)
- **Almacenamiento**: Mínimo 10GB libres
- **Procesador**: Intel i5 o AMD equivalente (mínimo)

### Requisitos de Software
- **Sistema Operativo**: Windows 10/11, macOS 10.14+, o Linux Ubuntu 18.04+
- **Java Development Kit (JDK)**: Version 11 o superior
- **Android Studio**: Versión más reciente (recomendado)
- **Android SDK**: API Level 27 (mínimo) hasta API Level 35
- **Git**: Para control de versiones
- **Conexión a Internet**: Para Firebase y descargas de dependencias

### Requisitos del Dispositivo Android
- **API Level**: 27 (Android 8.1) o superior
- **RAM**: Mínimo 2GB
- **Almacenamiento**: 100MB libres
- **Conexión**: Internet (WiFi o datos móviles)

---

## ⚙️ Configuración del Entorno de Desarrollo

### 1. Instalación de Android Studio

1. **Descargar Android Studio**
   ```
   https://developer.android.com/studio
   ```

2. **Ejecutar el instalador** y seguir las instrucciones

3. **Configurar SDK Manager**
   - Abrir Android Studio
   - Ir a `Tools > SDK Manager`
   - Instalar:
     - Android SDK Platform 35
     - Android SDK Platform 27
     - Android SDK Build-Tools 35.0.0
     - Google Play Services
     - Google Repository

### 2. Configuración de Variables de Entorno

**Windows:**
```batch
ANDROID_HOME=C:\Users\[TuUsuario]\AppData\Local\Android\Sdk
JAVA_HOME=C:\Program Files\Java\jdk-11
```

**macOS/Linux:**
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

### 3. Instalación de Git
```bash
# Windows (usando Chocolatey)
choco install git

# macOS (usando Homebrew)
brew install git

# Ubuntu/Debian
sudo apt-get install git
```

---

## 🗄️ Instalación de la Base de Datos

### Base de Datos Local (Room Database)

El proyecto utiliza **Room Database** para almacenamiento local. Las tablas se crean automáticamente:

#### Esquema de la Base de Datos

**Tabla: propiedades**
```sql
CREATE TABLE propiedades (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    descripcion TEXT NOT NULL,
    precio REAL NOT NULL,
    direccion TEXT NOT NULL,
    latitud REAL NOT NULL,
    longitud REAL NOT NULL,
    largo REAL NOT NULL,
    ancho REAL NOT NULL,
    area REAL NOT NULL,
    tipoPropiedad TEXT NOT NULL,
    dormitorios INTEGER NOT NULL,
    banos INTEGER NOT NULL,
    caracteristicas TEXT,
    fechaPublicacion INTEGER NOT NULL,
    usuarioId TEXT NOT NULL,
    estado TEXT NOT NULL,
    medioContacto TEXT
);
```

**Tabla: usuarios**
```sql
CREATE TABLE usuarios (
    id TEXT PRIMARY KEY,
    nombre TEXT NOT NULL,
    correo TEXT NOT NULL,
    telefono TEXT NOT NULL,
    rol TEXT NOT NULL,
    fechaRegistro INTEGER NOT NULL
);
```

**Tabla: visitas**
```sql
CREATE TABLE visitas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    propiedadId INTEGER NOT NULL,
    clienteId TEXT NOT NULL,
    vendedorId TEXT NOT NULL,
    fecha INTEGER NOT NULL,
    hora TEXT NOT NULL,
    estado TEXT NOT NULL,
    notas TEXT,
    FOREIGN KEY(propiedadId) REFERENCES propiedades(id),
    FOREIGN KEY(clienteId) REFERENCES usuarios(id),
    FOREIGN KEY(vendedorId) REFERENCES usuarios(id)
);
```

**Tabla: imagen_propiedad**
```sql
CREATE TABLE imagen_propiedad (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    propiedadId INTEGER NOT NULL,
    url TEXT NOT NULL,
    esPrincipal INTEGER NOT NULL DEFAULT 0,
    FOREIGN KEY(propiedadId) REFERENCES propiedades(id)
);
```

### Scripts de Inicialización

Los scripts se ejecutan automáticamente al inicializar la aplicación. Para datos de prueba, consultar la sección [Datos de Prueba](#-datos-de-prueba).

---

## 🔥 Configuración de Firebase

### 1. Crear Proyecto en Firebase

1. **Ir a Firebase Console**
   ```
   https://console.firebase.google.com/
   ```

2. **Crear nuevo proyecto**
   - Nombre: `ventabienesraices-[tu-id]`
   - Habilitar Google Analytics (opcional)

3. **Agregar aplicación Android**
   - Package name: `com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces`
   - App nickname: `VentaBienesRaices`
   - SHA-1: (Obtener desde Android Studio)

### 2. Configurar Firestore Database

1. **En Firebase Console, ir a Firestore Database**
2. **Crear base de datos en modo de prueba**
3. **Configurar reglas de seguridad**:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Permitir lectura y escritura autenticada
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

### 3. Configurar Authentication

1. **En Firebase Console, ir a Authentication**
2. **Habilitar proveedores de inicio de sesión**:
   - Email/Contraseña: ✅ Habilitado
   - Google: ✅ Habilitado (opcional)

### 4. Descargar google-services.json

1. **En configuración del proyecto, descargar `google-services.json`**
2. **Colocar el archivo en**: `app/`

### Estructura de Colecciones en Firestore

**Colección: usuarios**
```json
{
  "id": "string",
  "nombre": "string",
  "correo": "string",
  "telefono": "string",
  "rol": "string", // "cliente", "vendedor", "admin"
  "fechaRegistro": "timestamp"
}
```

**Colección: propiedades**
```json
{
  "id": "string",
  "titulo": "string",
  "descripcion": "string",
  "precio": "number",
  "ubicacion": {
    "direccion": "string",
    "latitud": "number",
    "longitud": "number"
  },
  "dimensiones": {
    "largo": "number",
    "ancho": "number",
    "area": "number"
  },
  "detalles": {
    "tipoPropiedad": "string",
    "dormitorios": "number",
    "banos": "number"
  },
  "caracteristicas": ["string"],
  "imagenes": ["string"],
  "usuarioId": "string",
  "fechaPublicacion": "timestamp",
  "estado": "string",
  "medioContacto": "string"
}
```

**Colección: visitas**
```json
{
  "id": "string",
  "propiedadId": "string",
  "clienteId": "string",
  "vendedorId": "string",
  "fecha": "timestamp",
  "hora": "string",
  "estado": "string", // "programada", "realizada", "cancelada"
  "notas": "string"
}
```

---

## 📥 Instalación del Código Fuente

### 1. Clonar el Repositorio

```bash
git clone https://github.com/EdMendez00/ProyectoPDM2025GT2Grupo3VentadeBienesRaces.git
cd ProyectoPDM2025GT2Grupo3VentadeBienesRaces
```

### 2. Estructura del Proyecto

```
ProyectoPDM2025GT2Grupo3VentadeBienesRaces/
├── app/
│   ├── src/main/java/com/example/proyectopdm2025_gt2_grupo3_ventadebienesraces/
│   │   ├── adapters/          # Adaptadores para RecyclerView
│   │   ├── local/             # Base de datos Room
│   │   │   ├── dao/           # Data Access Objects
│   │   │   ├── entity/        # Entidades de la BD
│   │   │   └── AppDatabase.kt # Configuración de BD
│   │   ├── model/             # Modelos de datos
│   │   ├── repository/        # Repositorios para manejo de datos
│   │   ├── viewmodel/         # ViewModels para MVVM
│   │   ├── LoginActivity.kt   # Actividad de login
│   │   ├── RegisterActivity.kt # Actividad de registro
│   │   ├── HomeActivity.kt    # Actividad principal
│   │   ├── DetallePropiedad.java # Detalle de propiedad
│   │   ├── HomeFragment.kt    # Fragment de inicio
│   │   ├── SearchFragment.kt  # Fragment de búsqueda
│   │   ├── AddFragment.kt     # Fragment para agregar
│   │   ├── FavoriteFragment.kt # Fragment de favoritos
│   │   └── ProfileFragment.kt # Fragment de perfil
│   ├── src/main/res/
│   │   ├── layout/           # Archivos de diseño XML
│   │   ├── values/           # Recursos (strings, colors, etc.)
│   │   └── drawable/         # Recursos gráficos
│   ├── build.gradle.kts      # Configuración de build
│   └── google-services.json  # Configuración Firebase
├── django_backend/           # Backend Django (básico)
├── build.gradle.kts          # Configuración global
├── settings.gradle.kts       # Configuración del proyecto
└── README.md                 # Documentación básica
```

### 3. Configuración del Proyecto en Android Studio

1. **Abrir Android Studio**
2. **Open an Existing Project**
3. **Seleccionar la carpeta del proyecto clonado**
4. **Esperar sincronización de Gradle**

### 4. Verificar Dependencias

El archivo `app/build.gradle.kts` contiene todas las dependencias necesarias:

```kotlin
dependencies {
    // Android Core
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    
    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    
    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    
    // ViewModel y LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")
    
    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}
```

---

## 🔨 Compilación y Generación del APK

### 1. Compilación en Modo Debug

```bash
# Desde la terminal en la raíz del proyecto
./gradlew assembleDebug

# En Windows
gradlew.bat assembleDebug
```

### 2. Compilación en Modo Release

1. **Generar keystore para firma**:
```bash
keytool -genkey -v -keystore my-release-key.keystore -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
```

2. **Configurar signing en build.gradle.kts**:
```kotlin
android {
    signingConfigs {
        release {
            storeFile file('my-release-key.keystore')
            storePassword 'your-store-password'
            keyAlias 'my-key-alias'
            keyPassword 'your-key-password'
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.release
        }
    }
}
```

3. **Compilar APK de release**:
```bash
./gradlew assembleRelease
```

### 3. Ubicación de los APK Generados

```
app/build/outputs/apk/debug/app-debug.apk
app/build/outputs/apk/release/app-release.apk
```

### 4. Generar Bundle para Google Play Store

```bash
./gradlew bundleRelease
```

El archivo bundle se genera en:
```
app/build/outputs/bundle/release/app-release.aab
```

---

## 📱 Instalación de la Aplicación

### 1. Instalación Directa (Debug)

**Desde Android Studio:**
1. Conectar dispositivo Android o iniciar emulador
2. Hacer clic en el botón "Run" (▶️)
3. Seleccionar dispositivo target
4. La app se instala automáticamente

**Desde línea de comandos:**
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 2. Instalación Manual (APK)

1. **Transferir APK al dispositivo**
2. **Habilitar "Fuentes desconocidas"** en Configuración > Seguridad
3. **Abrir el archivo APK** en el dispositivo
4. **Seguir las instrucciones de instalación**

### 3. Instalación a través de Google Play Store

1. **Subir el bundle (.aab)** a Google Play Console
2. **Configurar la información de la aplicación**
3. **Publicar en Play Store**

### 4. Verificación de Instalación

1. **Abrir la aplicación**
2. **Verificar splash screen**
3. **Probar funcionalidad de login**
4. **Confirmar conectividad con Firebase**

---

## 🌐 Configuración de Servicios Web

### Backend Django (Básico)

El proyecto incluye un backend Django básico en la carpeta `django_backend/`. Actualmente está mínimamente implementado.

#### Configuración Básica de Django

1. **Instalar Python y pip**
```bash
python --version  # Verificar Python 3.8+
pip --version
```

2. **Instalar Django**
```bash
cd django_backend/
pip install django
pip install djangorestframework
pip install django-cors-headers
```

3. **Configurar settings.py** (crear si no existe):
```python
# django_backend/settings.py
import os
from pathlib import Path

BASE_DIR = Path(__file__).resolve().parent.parent

SECRET_KEY = 'tu-clave-secreta-aqui'
DEBUG = True
ALLOWED_HOSTS = ['*']

INSTALLED_APPS = [
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'rest_framework',
    'corsheaders',
]

MIDDLEWARE = [
    'corsheaders.middleware.CorsMiddleware',
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]

ROOT_URLCONF = 'django_backend.urls'

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.sqlite3',
        'NAME': BASE_DIR / 'db.sqlite3',
    }
}

CORS_ALLOW_ALL_ORIGINS = True
```

4. **Ejecutar migraciones**:
```bash
python manage.py migrate
```

5. **Ejecutar servidor**:
```bash
python manage.py runserver 0.0.0.0:8000
```

### Servicios Web Principales

La aplicación utiliza principalmente **Firebase** como backend:

1. **Firebase Authentication**: Manejo de usuarios
2. **Firebase Firestore**: Base de datos NoSQL
3. **Firebase Storage**: Almacenamiento de imágenes (si se implementa)

### APIs Disponibles

Las operaciones se realizan directamente con Firebase desde la aplicación:

**Autenticación:**
- `FirebaseAuth.signInWithEmailAndPassword()`
- `FirebaseAuth.createUserWithEmailAndPassword()`
- `FirebaseAuth.signOut()`

**Firestore Operations:**
- `FirebaseFirestore.collection("propiedades").get()`
- `FirebaseFirestore.collection("usuarios").add()`
- `FirebaseFirestore.collection("visitas").where()`

---

## 🧪 Pruebas de Funcionalidad

### 1. Configuración del Entorno de Pruebas

**Crear dispositivo virtual (AVD):**
1. Abrir AVD Manager en Android Studio
2. Create Virtual Device
3. Seleccionar Pixel 6 (recomendado)
4. Seleccionar API 35 (Android 14)
5. Configurar RAM: 4GB

### 2. Plan de Pruebas

#### A. Pruebas de Autenticación

**Test Case 1: Registro de Usuario**
```
Pasos:
1. Abrir aplicación
2. Hacer clic en "Registrarse"
3. Llenar formulario:
   - Nombre: Juan Pérez
   - Apellido: González
   - Email: juan.perez@test.com
   - Contraseña: Test123456
   - Confirmar contraseña: Test123456
   - Aceptar términos: ✓
4. Hacer clic en "Registrarse"

Resultado esperado:
- Usuario creado exitosamente
- Redirección a pantalla principal
- Mensaje de bienvenida
```

**Test Case 2: Inicio de Sesión**
```
Pasos:
1. En pantalla de login
2. Ingresar email: juan.perez@test.com
3. Ingresar contraseña: Test123456
4. Hacer clic en "Iniciar Sesión"

Resultado esperado:
- Login exitoso
- Acceso a pantalla principal
- Navegación habilitada
```

#### B. Pruebas de Funcionalidad Principal

**Test Case 3: Visualización de Propiedades**
```
Pasos:
1. Login exitoso
2. Estar en HomeFragment
3. Verificar lista de propiedades

Resultado esperado:
- Lista de propiedades cargada
- Imágenes mostradas correctamente
- Información básica visible (precio, ubicación)
```

**Test Case 4: Búsqueda de Propiedades**
```
Pasos:
1. Navegar a SearchFragment
2. Ingresar criterio: "Casa"
3. Aplicar filtros de precio: $50,000 - $100,000
4. Buscar

Resultado esperado:
- Resultados filtrados mostrados
- Solo casas en rango de precio
- Funcionalidad de filtros operativa
```

**Test Case 5: Agregar Nueva Propiedad**
```
Pasos:
1. Navegar a AddFragment
2. Llenar formulario:
   - Título: Casa de Prueba
   - Descripción: Casa de prueba para testing
   - Precio: $75,000
   - Dirección: Calle Falsa 123
   - Tipo: Casa
   - Dormitorios: 3
   - Baños: 2
3. Agregar características
4. Guardar

Resultado esperado:
- Propiedad creada exitosamente
- Visible en lista de propiedades
- Datos guardados correctamente
```

**Test Case 6: Gestión de Favoritos**
```
Pasos:
1. En lista de propiedades
2. Hacer clic en ícono de corazón
3. Navegar a FavoriteFragment
4. Verificar propiedad guardada

Resultado esperado:
- Propiedad agregada a favoritos
- Visible en sección favoritos
- Funcionalidad de eliminar operativa
```

**Test Case 7: Programar Visita**
```
Pasos:
1. Abrir detalle de propiedad
2. Hacer clic en "Programar Visita"
3. Seleccionar fecha y hora
4. Agregar notas opcionales
5. Confirmar visita

Resultado esperado:
- Visita programada exitosamente
- Notificación al vendedor
- Visible en historial de visitas
```

**Test Case 8: Gestión de Perfil**
```
Pasos:
1. Navegar a ProfileFragment
2. Ver información personal
3. Editar número de teléfono
4. Guardar cambios

Resultado esperado:
- Información de perfil mostrada
- Cambios guardados exitosamente
- Datos actualizados en Firebase
```

### 3. Pruebas de Integración

#### Firebase Integration Tests

**Test de Conectividad:**
```kotlin
@Test
fun testFirebaseConnection() {
    val firestore = FirebaseFirestore.getInstance()
    val testDocument = firestore.collection("test").document("connectivity")
    
    testDocument.set(mapOf("test" to true))
        .addOnSuccessListener {
            // Conexión exitosa
            assert(true)
        }
        .addOnFailureListener {
            // Error de conexión
            assert(false)
        }
}
```

#### Room Database Tests

**Test de Inserción:**
```kotlin
@Test
fun testInsertPropiedad() {
    val propiedad = PropiedadEntity(
        titulo = "Test Casa",
        descripcion = "Casa de prueba",
        precio = 75000.0,
        direccion = "Test Address",
        // ... otros campos
    )
    
    propiedadDao.insertPropiedad(propiedad)
    val propiedades = propiedadDao.getAllPropiedades().getOrAwaitValue()
    
    assertThat(propiedades).contains(propiedad)
}
```

### 4. Pruebas de Performance

**Métricas a evaluar:**
- Tiempo de carga inicial: < 3 segundos
- Tiempo de login: < 2 segundos
- Carga de lista de propiedades: < 2 segundos
- Uso de memoria: < 150MB
- Uso de batería: Optimizado

### 5. Pruebas en Diferentes Dispositivos

**Dispositivos recomendados para prueba:**
- Pixel 6 (API 35) - Emulador
- Samsung Galaxy S21 - Físico
- Xiaomi Redmi Note 10 - Físico
- Dispositivo con API 27 - Compatibilidad mínima

---

## 📊 Datos de Prueba

### 1. Usuarios de Prueba

#### Usuario Administrador
```json
{
  "id": "admin001",
  "nombre": "María Admin",
  "correo": "admin@ventabienes.com",
  "telefono": "+503 7123-4567",
  "rol": "admin",
  "fechaRegistro": "2025-01-15T10:00:00Z"
}
```

#### Usuario Vendedor
```json
{
  "id": "vendor001",
  "nombre": "Carlos Vendedor",
  "correo": "carlos.vendedor@gmail.com",
  "telefono": "+503 7234-5678",
  "rol": "vendedor",
  "fechaRegistro": "2025-01-15T11:00:00Z"
}
```

#### Usuario Cliente
```json
{
  "id": "client001",
  "nombre": "Ana Cliente",
  "correo": "ana.cliente@gmail.com",
  "telefono": "+503 7345-6789",
  "rol": "cliente",
  "fechaRegistro": "2025-01-15T12:00:00Z"
}
```

### 2. Propiedades de Prueba

#### Casa Residencial
```json
{
  "id": "prop001",
  "titulo": "Casa Residencial en Colonia Escalón",
  "descripcion": "Hermosa casa de dos plantas en exclusiva colonia Escalón. Cuenta con amplio jardín, sala, comedor, cocina equipada, 4 dormitorios y 3 baños completos.",
  "precio": 185000,
  "ubicacion": {
    "direccion": "Colonia Escalón, San Salvador",
    "latitud": 13.7068,
    "longitud": -89.2257
  },
  "dimensiones": {
    "largo": 15.5,
    "ancho": 12.0,
    "area": 186.0
  },
  "detalles": {
    "tipoPropiedad": "Casa",
    "dormitorios": 4,
    "banos": 3
  },
  "caracteristicas": [
    "Jardín amplio",
    "Cochera para 2 vehículos",
    "Cocina equipada",
    "Aire acondicionado",
    "Seguridad 24/7"
  ],
  "imagenes": [
    "https://example.com/casa1-frente.jpg",
    "https://example.com/casa1-sala.jpg",
    "https://example.com/casa1-cocina.jpg"
  ],
  "usuarioId": "vendor001",
  "fechaPublicacion": "2025-01-15T13:00:00Z",
  "estado": "DISPONIBLE",
  "medioContacto": "WhatsApp: +503 7234-5678"
}
```

#### Apartamento Moderno
```json
{
  "id": "prop002",
  "titulo": "Apartamento Moderno en Torre Ejecutiva",
  "descripcion": "Moderno apartamento en torre ejecutiva con vista panorámica de la ciudad. Ubicado en zona céntrica con fácil acceso a centros comerciales y transporte público.",
  "precio": 95000,
  "ubicacion": {
    "direccion": "Centro Histórico, San Salvador",
    "latitud": 13.6969,
    "longitud": -89.1914
  },
  "dimensiones": {
    "largo": 10.0,
    "ancho": 8.5,
    "area": 85.0
  },
  "detalles": {
    "tipoPropiedad": "Apartamento",
    "dormitorios": 2,
    "banos": 2
  },
  "caracteristicas": [
    "Vista panorámica",
    "Piscina común",
    "Gimnasio",
    "Parqueo incluido",
    "Portón eléctrico"
  ],
  "imagenes": [
    "https://example.com/apt1-sala.jpg",
    "https://example.com/apt1-dormitorio.jpg",
    "https://example.com/apt1-vista.jpg"
  ],
  "usuarioId": "vendor001",
  "fechaPublicacion": "2025-01-14T09:30:00Z",
  "estado": "DISPONIBLE",
  "medioContacto": "Teléfono: +503 7234-5678"
}
```

#### Local Comercial
```json
{
  "id": "prop003",
  "titulo": "Local Comercial en Metrocentro",
  "descripcion": "Excelente local comercial en centro comercial Metrocentro. Ideal para negocio de ropa, restaurante o oficina. Alta afluencia de personas.",
  "precio": 125000,
  "ubicacion": {
    "direccion": "Metrocentro, San Salvador",
    "latitud": 13.7020,
    "longitud": -89.2272
  },
  "dimensiones": {
    "largo": 8.0,
    "ancho": 6.0,
    "area": 48.0
  },
  "detalles": {
    "tipoPropiedad": "Local Comercial",
    "dormitorios": 0,
    "banos": 1
  },
  "caracteristicas": [
    "Alta afluencia",
    "Aire acondicionado central",
    "Vitrina amplia",
    "Seguridad del centro comercial",
    "Parqueo abundante"
  ],
  "imagenes": [
    "https://example.com/local1-frente.jpg",
    "https://example.com/local1-interior.jpg"
  ],
  "usuarioId": "vendor001",
  "fechaPublicacion": "2025-01-13T14:15:00Z",
  "estado": "DISPONIBLE",
  "medioContacto": "Email: carlos.vendedor@gmail.com"
}
```

### 3. Visitas Programadas

#### Visita Casa Residencial
```json
{
  "id": "visit001",
  "propiedadId": "prop001",
  "clienteId": "client001",
  "vendedorId": "vendor001",
  "fecha": "2025-01-20T15:00:00Z",
  "hora": "15:00",
  "estado": "programada",
  "notas": "Cliente interesado en conocer el jardín y la cocina"
}
```

#### Visita Apartamento
```json
{
  "id": "visit002",
  "propiedadId": "prop002",
  "clienteId": "client001",
  "vendedorId": "vendor001",
  "fecha": "2025-01-18T10:30:00Z",
  "hora": "10:30",
  "estado": "realizada",
  "notas": "Cliente muy interesado, solicita información de financiamiento"
}
```

### 4. Script de Inserción de Datos

Para insertar datos de prueba en Firebase, usar el siguiente código:

```kotlin
// En una actividad o fragment
private fun insertTestData() {
    val firestore = FirebaseFirestore.getInstance()
    
    // Insertar usuarios de prueba
    val usuarios = listOf(
        hashMapOf(
            "id" to "admin001",
            "nombre" to "María Admin",
            "correo" to "admin@ventabienes.com",
            "telefono" to "+503 7123-4567",
            "rol" to "admin",
            "fechaRegistro" to Timestamp.now()
        ),
        // ... más usuarios
    )
    
    usuarios.forEach { usuario ->
        firestore.collection("usuarios")
            .document(usuario["id"] as String)
            .set(usuario)
    }
    
    // Insertar propiedades de prueba
    val propiedades = listOf(
        hashMapOf(
            "titulo" to "Casa Residencial en Colonia Escalón",
            "descripcion" to "Hermosa casa de dos plantas...",
            "precio" to 185000,
            // ... más campos
        ),
        // ... más propiedades
    )
    
    propiedades.forEach { propiedad ->
        firestore.collection("propiedades")
            .add(propiedad)
    }
}
```

### 5. Credenciales de Prueba

**Para testing usar estas credenciales:**

```
Email: admin@ventabienes.com
Contraseña: Admin123456

Email: carlos.vendedor@gmail.com
Contraseña: Vendor123456

Email: ana.cliente@gmail.com
Contraseña: Client123456
```

---

## 👥 Información de Autoría

### Equipo de Desarrollo

#### **Coordinador General y Desarrollador Backend**
- **Nombre**: Eduardo Méndez
- **GitHub**: [@EdMendez00](https://github.com/EdMendez00)
- **Responsabilidades**:
  - Coordinación del proyecto
  - Arquitectura de Firebase
  - Backend Django
  - Integración de servicios

#### **Desarrollador Frontend Principal**
- **Nombre**: Carlos Argueta
- **GitHub**: [@cargueta01](https://github.com/cargueta01)
- **Responsabilidades**:
  - Diseño de interfaces principales
  - LoginActivity y RegisterActivity
  - HomeFragment y navegación
  - Implementación de Room Database

#### **Desarrollador de Funcionalidades Core**
- **Nombre**: Samuel Alas
- **GitHub**: [@Samuelalas200](https://github.com/Samuelalas200)
- **Responsabilidades**:
  - SearchFragment y filtros
  - AddFragment para propiedades
  - Integración con Firebase Firestore
  - Gestión de imágenes

#### **Desarrollador de Características Avanzadas**
- **Nombre**: Pendragon503
- **GitHub**: [@Pendragon503](https://github.com/Pendragon503)
- **Responsabilidades**:
  - FavoriteFragment
  - ProfileFragment
  - Sistema de visitas
  - DetallePropiedad (Java)

### Distribución por Pantallas/Módulos

#### **Módulo de Autenticación**
**Autor Principal**: Carlos Argueta
- `LoginActivity.kt` - Pantalla de inicio de sesión
- `RegisterActivity.kt` - Pantalla de registro
- `WelcomeActivity.kt` - Pantalla de bienvenida
- Layout: `activity_login.xml`, `activity_register.xml`, `activity_welcome.xml`

#### **Módulo Principal/Navegación**
**Autor Principal**: Carlos Argueta
- `HomeActivity.kt` - Actividad contenedora principal
- `HomeFragment.kt` - Pantalla principal con propiedades
- Layout: `activity_home.xml`, `fragment_home.xml`
- **Funcionalidades**: Navegación entre fragments, lista de propiedades

#### **Módulo de Búsqueda**
**Autor Principal**: Samuel Alas
- `SearchFragment.kt` - Pantalla de búsqueda y filtros
- Layout: `fragment_search.xml`
- **Funcionalidades**: Filtros por precio, tipo, ubicación; búsqueda de texto

#### **Módulo de Publicación**
**Autor Principal**: Samuel Alas
- `AddFragment.kt` - Pantalla para agregar propiedades
- Layout: `fragment_add.xml`
- **Funcionalidades**: Formulario de nueva propiedad, subida de imágenes, validaciones

#### **Módulo de Favoritos**
**Autor Principal**: Pendragon503
- `FavoriteFragment.kt` - Pantalla de propiedades favoritas
- Layout: `fragment_favorite.xml`, `fragment_favorites.xml`
- **Funcionalidades**: Lista de favoritos, eliminar favoritos, navegación a detalles

#### **Módulo de Perfil**
**Autor Principal**: Pendragon503
- `ProfileFragment.kt` - Pantalla de perfil de usuario
- Layout: `fragment_profile.xml`
- **Funcionalidades**: Información personal, mis publicaciones, configuración

#### **Módulo de Detalles**
**Autor Principal**: Pendragon503
- `DetallePropiedad.java` - Pantalla de detalle de propiedad
- Layout: `activity_detalle_propiedad.xml`
- **Funcionalidades**: Vista completa de propiedad, galería de imágenes, contacto

#### **Módulo de Base de Datos**
**Autor Principal**: Carlos Argueta
- `AppDatabase.kt` - Configuración de Room
- `local/entity/` - Entidades de base de datos
- `local/dao/` - Data Access Objects
- **Funcionalidades**: Almacenamiento local, sincronización

#### **Módulo de Repositorios**
**Autor Principal**: Samuel Alas
- `repository/PropiedadRepository.kt` - Repositorio de propiedades
- `repository/UsuarioRepository.kt` - Repositorio de usuarios
- `repository/VisitaRepository.kt` - Repositorio de visitas
- **Funcionalidades**: Abstracción de acceso a datos, Firebase integration

#### **Módulo de ViewModels**
**Autores**: Equipo completo
- `viewmodel/PropiedadViewModel.kt` - ViewModel principal
- **Funcionalidades**: Lógica de negocio, estado de UI

#### **Módulo de Adaptadores**
**Autores**: Samuel Alas, Carlos Argueta
- `adapters/PublicacionAdapter.kt` - Adaptador para lista de propiedades
- `adapters/FeatureAdapter.kt` - Adaptador para características
- Layout: `item_publicacion.xml`, `item_feature.xml`

#### **Módulo de Backend**
**Autor Principal**: Eduardo Méndez
- `django_backend/authentication.py` - Autenticación Django
- **Funcionalidades**: APIs REST, autenticación, administración

### Contribuciones Específicas

#### **Carlos Argueta (@cargueta01)**
```
Commits principales:
- Implementación de sistema de autenticación
- Diseño de interfaz principal
- Configuración de Room Database
- Navegación entre pantallas
Archivos modificados: 25+
Líneas de código: 2,500+
```

#### **Samuel Alas (@Samuelalas200)**
```
Commits principales:
- Sistema de búsqueda y filtros
- Módulo de publicación de propiedades
- Integración con Firebase Firestore
- Repositorios de datos
Archivos modificados: 20+
Líneas de código: 2,200+
```

#### **Pendragon503 (@Pendragon503)**
```
Commits principales:
- Sistema de favoritos
- Gestión de perfil de usuario
- Pantalla de detalles de propiedad
- Sistema de visitas programadas
Archivos modificados: 18+
Líneas de código: 2,000+
```

#### **Eduardo Méndez (@EdMendez00)**
```
Commits principales:
- Configuración inicial del proyecto
- Integración con Firebase
- Backend Django (estructura base)
- Coordinación y merge de ramas
Archivos modificados: 15+
Líneas de código: 1,500+
```

### Metodología de Desarrollo

**Herramientas utilizadas**:
- **Control de versiones**: Git/GitHub
- **IDE**: Android Studio
- **Comunicación**: WhatsApp, Google Meet
- **Gestión**: GitHub Issues y Projects
- **Testing**: Emuladores Android, dispositivos físicos

**Proceso de desarrollo**:
1. **Sprint Planning**: Reuniones semanales
2. **Feature Branches**: Una rama por funcionalidad
3. **Code Review**: Revisión de pull requests
4. **Testing**: Pruebas individuales y de integración
5. **Merge**: Integración en rama principal

### Reconocimientos

**Agradecimientos especiales**:
- **ING. CESAR AUGUSTO GONZALEZ RODRIGUEZ** - Tutor del proyecto
- **Universidad de El Salvador** - Infraestructura educativa
- **Facultad de Ingeniería y Arquitectura** - Recursos académicos
- **Comunidad Android** - Documentación y soporte

---

## 🔧 Solución de Problemas

### Problemas Comunes de Instalación

#### 1. Error de Sincronización de Gradle

**Error**: `Gradle sync failed`

**Solución**:
```bash
# Limpiar caché de Gradle
./gradlew clean

# En Windows
gradlew.bat clean

# Invalidar caché en Android Studio
File > Invalidate Caches and Restart
```

#### 2. Error de Firebase

**Error**: `FirebaseApp initialization unsuccessful`

**Solución**:
1. Verificar que `google-services.json` esté en `app/`
2. Verificar configuración en `build.gradle.kts`:
```kotlin
id("com.google.gms.google-services")
```
3. Verificar dependencias de Firebase:
```kotlin
implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
```

#### 3. Error de Room Database

**Error**: `Cannot find implementation for database`

**Solución**:
1. Verificar anotaciones `@Entity` y `@Dao`
2. Agregar procesador de anotaciones:
```kotlin
kapt("androidx.room:room-compiler:2.6.1")
```
3. Rebuild project

#### 4. Error de Permisos Android

**Error**: Aplicación no instala por permisos

**Solución**:
1. Habilitar "Fuentes desconocidas" en dispositivo
2. Verificar permisos en `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

#### 5. Error de Memoria Insuficiente

**Error**: `OutOfMemoryError` durante compilación

**Solución**:
1. Aumentar memoria de Gradle en `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=512m
```
2. Cerrar aplicaciones innecesarias

### Problemas de Funcionalidad

#### 1. Login No Funciona

**Diagnóstico**:
- Verificar conexión a internet
- Verificar configuración de Firebase Auth
- Revisar reglas de Firestore

**Solución**:
1. Verificar credenciales en Firebase Console
2. Verificar reglas de Firestore:
```javascript
allow read, write: if request.auth != null;
```

#### 2. Propiedades No Cargan

**Diagnóstico**:
- Verificar datos en Firestore
- Verificar permisos de lectura
- Revisar logs de aplicación

**Solución**:
1. Insertar datos de prueba
2. Verificar consultas en `PropiedadRepository.kt`

#### 3. Imágenes No Se Muestran

**Diagnóstico**:
- Verificar URLs de imágenes
- Verificar permisos de internet
- Revisar configuración de Glide

**Solución**:
1. Verificar dependencia de Glide
2. Agregar permisos de internet
3. Usar URLs válidas de imágenes

### Logs de Depuración

#### Habilitar Logs Detallados

```kotlin
// En Application class o MainActivity
if (BuildConfig.DEBUG) {
    FirebaseFirestore.setLoggingEnabled(true)
    Log.d("VentaBienesRaices", "Debug mode enabled")
}
```

#### Comandos Útiles de ADB

```bash
# Ver logs de aplicación
adb logcat | grep VentaBienesRaices

# Limpiar datos de aplicación
adb shell pm clear com.example.proyectopdm2025_gt2_grupo3_ventadebienesraces

# Instalar APK forzadamente
adb install -r app-debug.apk

# Ver dispositivos conectados
adb devices
```

### Contacto para Soporte

**En caso de problemas adicionales, contactar al equipo de desarrollo**:

- **Repositorio**: [GitHub Issues](https://github.com/EdMendez00/ProyectoPDM2025GT2Grupo3VentadeBienesRaces/issues)
- **Email de soporte**: eduardo.mendez@estudiantes.ues.edu.sv
- **Documentación adicional**: Consultar README.md del proyecto

---

## 📋 Checklist de Instalación

### Pre-instalación
- [ ] Android Studio instalado
- [ ] SDK Android configurado (API 27-35)
- [ ] Java JDK 11+ instalado
- [ ] Git instalado y configurado
- [ ] Cuenta de Firebase creada

### Configuración del Proyecto
- [ ] Repositorio clonado exitosamente
- [ ] Proyecto abierto en Android Studio
- [ ] Sincronización de Gradle completada
- [ ] `google-services.json` configurado
- [ ] Firebase Auth habilitado
- [ ] Firebase Firestore configurado

### Compilación y Testing
- [ ] Compilación debug exitosa
- [ ] APK generado correctamente
- [ ] Pruebas en emulador realizadas
- [ ] Pruebas en dispositivo físico realizadas
- [ ] Datos de prueba insertados

### Funcionalidades Verificadas
- [ ] Login/Register funcionando
- [ ] Lista de propiedades cargando
- [ ] Búsqueda y filtros operativos
- [ ] Agregar propiedad funcional
- [ ] Sistema de favoritos operativo
- [ ] Perfil de usuario funcionando
- [ ] Detalles de propiedad accesibles

### Post-instalación
- [ ] Base de datos poblada con datos de prueba
- [ ] Usuarios de prueba creados
- [ ] Funcionalidades principales probadas
- [ ] Rendimiento verificado
- [ ] Documentación revisada

---

**Fecha de última actualización**: 15 de Enero de 2025  
**Versión del manual**: 1.0  
**Versión de la aplicación**: 1.0  

---

## 📞 Información de Contacto

**Universidad de El Salvador**  
Facultad de Ingeniería y Arquitectura  
Escuela de Ingeniería de Sistemas Informáticos  

**Proyecto**: Aplicación de Venta de Bienes Raíces  
**Materia**: PDM115 - Programación Para Dispositivos Móviles  
**Ciclo**: I-2025  
**Grupo Teórico**: 02  
**Grupo de Proyecto**: 03  

**Tutor**: ING. CESAR AUGUSTO GONZALEZ RODRIGUEZ  

---

*Este manual cubre todos los aspectos necesarios para la instalación, configuración y testing del sistema de venta de bienes raíces. Para soporte adicional, consultar la documentación del proyecto en GitHub o contactar al equipo de desarrollo.*
