from flask import Blueprint, abort, request, render_template,redirect, flash, jsonify
import  requests
import json
import os
from flask import flash

router = Blueprint('router', __name__)

@router.route('/')
def home():
    return render_template('templateL.html') 


@router.route('/vet/lista')
def listaVet(msg=''):
    r = requests.get('http://localhost:8080/myapp/vet/list')
    #print(type(r,json()))
    data = r.json()
    return render_template("vet/lista.html", lista = data["data"], message = msg)

@router.route('/vet/registrar')
def registrarVet():
    r=requests.get('http://localhost:8080/myapp/vet/list')
    data = r.json()
    return render_template('vet/guardar.html', lista = data["data"])


@router.route('/vet/save', methods=['POST'])
def saveVet():
    headers = {'Content-Type': 'application/json'}
    form = request.form

    dataF = {
        "nombre": form["nombre"],
        "nombre_Veterinario": form["nombre_Veterinario"],
        "direccion": form["direccion"],
        "telefono": form["telefono"],
        "latitud": form["latitud"],
        "longitud": form["longitud"]

        
    }

    try:
        # Realiza la solicitud POST al servidor Java
        r = requests.post('http://localhost:8080/myapp/vet/save', data=json.dumps(dataF), headers=headers)
        dat = r.json()

        # Verifica el código de estado
        if r.status_code == 200:
            flash('Vet registrado con éxito', category='info')
        else:
            error_message = dat.get('data', 'Error al registrar el veterinaria')
            flash(error_message, category='error')

        # Redirige a la lista después de procesar la solicitud
        return redirect('/vet/lista')

    except requests.exceptions.RequestException as e:
        # Maneja excepciones de la solicitud
        flash(f'Error al conectar con el servicio: {str(e)}', category='error')
        return redirect('/vet/lista')

@router.route('/get_etiquetas')
def get_etiquetas():
    ruta_json = 'C:\\Users\\lisbe\\Desktop\\Practica3\\jersey-service\\media\\grafo.json'
    with open(ruta_json, 'r') as file:
        grafo_json = json.load(file)
    return jsonify(grafo_json['etiquetas'])

@router.route('/get_matriz')
def get_matriz():
    # Ruta donde se encuentra el archivo JSON generado por Java
    ruta_json = 'C:\\Users\\lisbe\\Desktop\\Practica3\\jersey-service\\media\\grafo.json'

    # Verificar si el archivo existe
    if not os.path.exists(ruta_json):
        return jsonify({"error": "El archivo JSON no existe en la ruta proporcionada."}), 404

    # Leer el archivo JSON que contiene el grafo
    with open(ruta_json, 'r') as file:
        grafo_json = json.load(file)

    # Obtener las aristas y las etiquetas de los vértices
    etiquetas = grafo_json['etiquetas']
    aristas = grafo_json['aristas']
    
    # Inicializar la matriz de adyacencia
    nro_vertices = len(etiquetas)
    matriz_adyacencia = [[float('inf')] * nro_vertices for _ in range(nro_vertices)]

    # Rellenar la matriz de adyacencia con los datos de las aristas
    for arista in aristas:
        from_vertice = arista['from'] - 1  # Ajuste 0-indexed
        to_vertice = arista['to'] - 1      # Ajuste 0-indexed
        weight = arista['weight']
        matriz_adyacencia[from_vertice][to_vertice] = weight

    # Convertir la matriz de adyacencia a JSON y devolverla al frontend
    return jsonify(matriz_adyacencia)



@router.route('/matriz')
def mostrar_matriz():
    ruta_json = 'C:\\Users\\lisbe\\Desktop\\Practica3\\jersey-service\\media\\grafo.json'

    # Verificar si el archivo existe
    if not os.path.exists(ruta_json):
        return jsonify({"error": "El archivo JSON no existe en la ruta proporcionada."}), 404

    # Leer el archivo JSON
    with open(ruta_json, 'r') as file:
        grafo_json = json.load(file)

    # Extraer solo los nombres de las etiquetas
    etiquetas = [etiqueta['label'] for etiqueta in grafo_json['etiquetas']]

    # Crear la matriz de adyacencia
    matriz = [[float('inf')] * len(etiquetas) for _ in range(len(etiquetas))]

    # Rellenar la matriz con las aristas
    for arista in grafo_json['aristas']:
        from_vertice = arista['from'] - 1  # Ajuste a índice 0
        to_vertice = arista['to'] - 1
        weight = arista['weight']
        matriz[from_vertice][to_vertice] = weight

    # Reemplazar infinito con "-"
    matriz = [["-" if valor == float('inf') else valor for valor in fila] for fila in matriz]

    # Renderizar la plantilla con los datos
    return render_template('/vet/ady.html', etiquetas=etiquetas, matriz=matriz, enumerate=enumerate)










