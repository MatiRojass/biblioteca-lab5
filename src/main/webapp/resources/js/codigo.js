/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


//Ejercicio 1
//Mostramos en pantalla el mensaje "Hola Mundo",
// luego se muestra el mensaje "Soy el primer Script"
function saludar(){
    alert("Hola Mundo");
    alert("Soy el primer Script");
}


const cintaCarrusel = document.querySelector(".libros-carrusel"); 
const boton_siguiente = document.getElementById("btn-siguiente");
const boton_anterior = document.getElementById("btn-anterior");

let carruselActual = 0; 

if (boton_anterior !== null && boton_siguiente !== null) {
    boton_siguiente.addEventListener("click", () =>{
    if (carruselActual === 3) {
        carruselActual = 0;
    }
    else {
        carruselActual++;
    }
    
    let desplazamiento = carruselActual * -250;
    cintaCarrusel.style.transform = `translateX(${desplazamiento}px)`;
    
});

boton_anterior.addEventListener("click", () =>{
    if (carruselActual === 0) {
        carruselActual = 3;
    }
    else {
        carruselActual--;
    }
    
    let desplazamiento = carruselActual * -250;
    cintaCarrusel.style.transform = `translateX(${desplazamiento}px)`;
});
}



//Ejercicio 3

const btnLeerMas = document.getElementById("leerMas");
const textoExtra = document.getElementById("txt-extra");

if (btnLeerMas !== null && textoExtra !==null) {
    btnLeerMas.addEventListener("click", (evento) => {
    //saca comportamiento de tag a con href = #    
    evento.preventDefault();
    
    //removemos clase "ocultar" para que se vea el texto
    textoExtra.classList.remove("ocultar");
    //agregamos clase "ocultar" para esconder boton.
  btnLeerMas.classList.add("ocultar");
});
}
    
//Ejercicio 4
//renderiza toda la pagina html y luego hace la funcionalidad js
//Comentamos para usar la validacion HTML del ejercicio 5
/*
document.addEventListener("DOMContentLoaded", function(){
    
    const formulario = document.getElementById("formulario"); 
    
    formulario.addEventListener("submit", function (e) {
        
        const nombre = document.getElementById("nombre").value.trim();
        const dni = document.getElementById("dni").value.trim();
        const fecha = document.getElementById("fecha").value;
        const email = document.getElementById("email").value.trim();
        
        let errores = [];
        
        const expRNombre = /^[a-zA-Z\s]+$/ ;
        
        //validacdion de nombre
        if (nombre === "") {
            errores.push("Nombre no puede estar vacio.");
        }else if (!expRNombre.test(nombre)) {
            errores.push("Nombre solo debe contener caracteres alfabeticos.");
        }
        
        //validacion de dni
        if (dni === "" || isNaN(dni) || Number(dni) <= 0) {
            errores.push("Dni debe ser un numero valido mayor a 0.");
        }
        
        //validacion fecha 
        if (fecha === "") {
            errores.push("Fecha no puede ser vacia. Seleccione fecha.");
        }
        
        //validacion email
        const expRMail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
       
        if (email === "") {
            errores.push("Email no puede ser vacio.");
        }else if(!expRMail.test(email)) {
            errores.push("Ingrese direccion email valida.");
        }
        
        //si vector errores esta cargado, no mandamos. Si no tiene errores, mandamos
        if (errores.length > 0) {
            e.preventDefault(); //no mandamos el formulario
            
            //Mostramos los errores
            alert("Corrija los Errores de Formulario: \n" + errores.join("\n- "));
        } else {
            alert("Formulario validado con exito");
        }

        
    });
});
*/

//Ejercicio 6 y 7

document.addEventListener("DOMContentLoaded", function(){
    //seleccionamos los elementos
    const formulario = document.getElementById("add-clientes");
    const contenedor = document.getElementById("contenedorVacio-clientes");
    
    formulario.addEventListener("submit", function(e){
        e.preventDefault();
        
        //capturamos los valores de los campos del form
        const nombre = document.getElementById("nombre").value;
        const direccion = document.getElementById("direccion").value;
        const telefono = document.getElementById("telefono").value;
        
        //creamos tag html para la card del nuevo cliente
        const nuevoCliente = document.createElement("article");
        nuevoCliente.classList.add("card-libro"); //le agregamos la clase css
        
        //indicamos tags faltantes dentro de la card de cliente nuevo
        nuevoCliente.innerHTML = `
        <button class="btn-eliminar">X</button>
        <h3>Nombre: ${nombre}</h3>
        <p>Direccion: ${direccion} </p>
        <p>Telefono: ${telefono}</p>
        `;
        
        //funcionalidad para borrar tarjeta cliente
        const btnBorrar = nuevoCliente.querySelector(".btn-eliminar");
        
        btnBorrar.addEventListener("click", function (){
            nuevoCliente.remove();
        });
        
        //inyectamos como nodo hijo en el contenedor
        contenedor.appendChild(nuevoCliente);
       
        formulario.reset();
    });
    
});

