import os
import re

def replace_text_in_file(file_path, old_text, new_text):
    with open(file_path, 'r', encoding='ISO-8859-1') as file:
        content = file.read()

    content = content.replace(old_text, new_text)

    with open(file_path, 'w', encoding='utf-8') as file:
        file.write(content)


def process_files_in_folder(folder_path, old_text_file_path, new_text_file_path):
    with open(old_text_file_path, 'r', encoding='utf-8') as old_text_file:
        old_text = old_text_file.read()

    with open(new_text_file_path, 'r', encoding='utf-8') as new_text_file:
        new_text = new_text_file.read()

    for root, dirs, files in os.walk(folder_path):
        for file_name in files:
            if file_name.endswith('.java'):
                file_path = os.path.join(root, file_name)
                replace_text_in_file(file_path, old_text, new_text)

if __name__ == "__main__":
    folder_path = "C:\\Users\\jartu\\Documents\\GitHub\\compilador\\Dartam\\src\\analizadorSintactico\\symbols"  # Reemplaza con la ruta de tu carpeta
    old_text_file_path = "C:\\Users\\jartu\\Documents\\GitHub\\compilador\\Dartam\\src\\analizadorSintactico\\old.txt"  # Reemplaza con la ruta de tu archivo de texto antiguo
    new_text_file_path = "C:\\Users\\jartu\\Documents\\GitHub\\compilador\\Dartam\\src\\analizadorSintactico\\new.txt"  # Reemplaza con la ruta de tu archivo de texto nuevo

    process_files_in_folder(folder_path, old_text_file_path, new_text_file_path)

    print("Proceso completado. Se ha realizado la sustitución en todos los archivos .java en la carpeta.")
