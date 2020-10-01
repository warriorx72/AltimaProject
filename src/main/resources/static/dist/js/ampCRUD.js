
$(document).ready(function () {

	listarClasificacion();
	listarAlmacenesfisicos();
	listarAlmaceneslogicos();

});
$('#detalleClasificacion').on('shown.bs.modal', function () {
	$(document).off('focusin.modal');
});

function agregarClasificacion(){
	Swal.fire({
		  title: 'Nueva clasificacion',
		  html:
			  '<div class="row">'+
				  '<div class="form-group col-sm-12">'+
				  	'<label for="descripcionMovimiento">Nombre</label>'+
				  	'<input type="text" class="form-control" id="clasificacion" name="clasificacion" placeholder="Clasificacion">'+
				  '</div>'+
				  
			  '</div>',
		  inputAttributes: {
		    autocapitalize: 'off'
		  },
		  showCancelButton: true,
		  confirmButtonText: 'Agregar',
		  cancelButtonText: 'Cancelar',
		  showLoaderOnConfirm: true,
		  preConfirm: (clasificacion) => {
			  
			  if(document.getElementById("clasificacion").value.length<1){
					Swal.showValidationMessage(
							`Complete todos los campos`
					)
				}
		  
		  },
		  allowOutsideClick: () => !Swal.isLoading()
		}).then((result) => {
		  if (result.value && document.getElementById("clasificacion").value ) {
				var clasificacion = document.getElementById("clasificacion").value;
			  $.ajax({
					type: "GET",
					url: "/verificar-duplicado-amp",
					data: {
						'Lookup': clasificacion,
						'Tipo': "Clasificacion"


					}

				}).done(function (data) {
					if(data==false){

						$.ajax({
							type: "POST",
							url: "/guardar-catalogo-amp",
							data: {
								"_csrf": $('#token').val(),
								'clasificacion': clasificacion

							}

						}).done(function (data) {
							listarClasificacion();
						});
						Swal.fire({
							position: 'center',
							icon: 'success',
							title: 'Insertado correctamente',
							showConfirmButton: false,
							timer: 1250
						})
						// / window.setTimeout(function(){location.reload()}, 2000);
					}// /fin segundoif
					else{
						Swal.fire({
							position: 'center',
							icon: 'error',
							title: 'registro duplicado no se ha insertado',
							showConfirmButton: false,
							timer: 1250
						})

					}
				});
		  }
		});
}


function agregarLinea(){
	Swal.fire({
		  title: 'Nueva Linea',
		  html:
			  '<div class="row">'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="descripcionMovimiento">Nombre</label>'+
				  	'<input type="text" class="form-control" id="linea" name="linea" placeholder="Linea">'+
				  '</div>'+
				  
				  '<div class="form-group col-sm-6">'+
				  	'<label for="ubicacionTalla">Clasificaci&oacute;n</label>'+
				  	'<select class="form-control" id="clasificacion" name="clasificacion">'+
				      
				   '</select>'+
				  '</div>'+
				  
			  '</div>',
		  inputAttributes: {
		    autocapitalize: 'off'
		  },
		  showCancelButton: true,
		  confirmButtonText: 'Agregar',
		  cancelButtonText: 'Cancelar',
		  showLoaderOnConfirm: true,
		  preConfirm: (linea) => {
			  
			  if(document.getElementById("linea").value.length<1 ){
					Swal.showValidationMessage(
							`Complete todos los campos`
					)
				}
		  
		  },
		  allowOutsideClick: () => !Swal.isLoading()
		}).then((result) => {
		  if (result.value && document.getElementById("linea").value ) {
				var linea = document.getElementById("linea").value;
				var clasificacion = document.getElementById("clasificacion").value;
				
			  $.ajax({
					type: "GET",
					url: "/verificar-duplicado-amp",
					data: {
						'Lookup': linea,
						'Tipo': "Linea"


					}

				}).done(function (data) {
					if(data==false){

						$.ajax({
							type: "POST",
							url: "/guardar-catalogo-amp",
							data: {
								"_csrf": $('#token').val(),
								'id_clasificacion': clasificacion,
								'linea':linea

							}

						}).done(function (data) {
							listarClasificacion();
						});
						Swal.fire({
							position: 'center',
							icon: 'success',
							title: 'Insertado correctamente',
							showConfirmButton: false,
							timer: 1250
						})
						// / window.setTimeout(function(){location.reload()}, 2000);
					}// /fin segundoif
					else{
						Swal.fire({
							position: 'center',
							icon: 'error',
							title: 'registro duplicado no se ha insertado',
							showConfirmButton: false,
							timer: 1250
						})

					}
				});
		  }
		});
	
	$.ajax({
		method: "GET",
		url: "/listar-amp",
		data:{
			"Tipo":"Clasificacion"
		} ,
		success: (data) => {
			$.each(data, function(key, val) {
	    		$('#clasificacion').append('<option value="' + val.idLookup + '">'+val.nombreLookup+'</option>');})
	    		//$('.selectpicker').selectpicker(["refresh"]);
		},
		error: (e) => {

		}
	})
}


function agregarMovimiento(){
	Swal.fire({
		  title: 'Nuevo movimiento',
		  html:
			  '<div class="row">'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="descripcionMovimiento">Nombre</label>'+
				  	'<input type="text" class="form-control" id="movimiento" name="movimiento" placeholder="Movimiento">'+
				  '</div>'+
				  
				  '<div class="form-group col-sm-6">'+
				  	'<label for="ubicacionTalla">Tipo</label>'+
				  	'<select class="form-control" id="tipo" name="tipo">'+
				  	'<option value="Entrada">Entrada</option>'+
				    '<option value="Salida">Salida</option>'+
				   '</select>'+
				  '</div>'+
				  
			  '</div>',
		  inputAttributes: {
		    autocapitalize: 'off'
		  },
		  showCancelButton: true,
		  confirmButtonText: 'Agregar',
		  cancelButtonText: 'Cancelar',
		  showLoaderOnConfirm: true,
		  preConfirm: (linea) => {
			  
			  if(document.getElementById("movimiento").value.length<1 ){
					Swal.showValidationMessage(
							`Complete todos los campos`
					)
				}
		  
		  },
		  allowOutsideClick: () => !Swal.isLoading()
		}).then((result) => {
		  if (result.value && document.getElementById("movimiento").value ) {
				var movimiento = document.getElementById("movimiento").value;
				var tipo = document.getElementById("tipo").value;
				
			  $.ajax({
					type: "GET",
					url: "/verificar-duplicado-amp",
					data: {
						'Lookup': movimiento,
						'Tipo': "Movimiento"


					}

				}).done(function (data) {
					if(data==false){

						$.ajax({
							type: "POST",
							url: "/guardar-catalogo-amp",
							data: {
								"_csrf": $('#token').val(),
								'movimiento': movimiento,
								'tipo':tipo

							}

						}).done(function (data) {
							listarClasificacion();
						});
						Swal.fire({
							position: 'center',
							icon: 'success',
							title: 'Insertado correctamente',
							showConfirmButton: false,
							timer: 1250
						})
						// / window.setTimeout(function(){location.reload()}, 2000);
					}// /fin segundoif
					else{
						Swal.fire({
							position: 'center',
							icon: 'error',
							title: 'registro duplicado no se ha insertado',
							showConfirmButton: false,
							timer: 1250
						})

					}
				});
		  }
		});
	

}

function agregarAlmacen(){
	
	Swal.fire({
		  title: 'Nuevo almacen',
		  html:
			  '<div class="row">'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="descripcionMovimiento">Almacen</label>'+
				  	'<input type="text" class="form-control" id="almacen" name="almacen" placeholder="Almacen">'+
				  '</div>'+
			  
				  '<div class="form-group col-sm-6">'+
				  	'<label for="descripcionMovimiento">Encargado</label>'+
				  	'<input type="text" class="form-control" id="encargado" name="encargado" placeholder="Encargado">'+
				  '</div>'+
				  
				  '<div class="form-group col-sm-6">'+
				  	'<label for="descripcionMovimiento">Movimiento Entrada</label>'+
				  	'<input type="text" class="form-control" id="entreda" name="entreda" placeholder="Entrada">'+
				  '</div>'+
				  
				  '<div class="form-group col-sm-6">'+
				  	'<label for="descripcionMovimiento">Movimiento Salida</label>'+
				  	'<input type="text" class="form-control" id="salida" name="salida" placeholder="Salida">'+
				  '</div>'+
				  
				 
				  
			  '</div>',
		  inputAttributes: {
		    autocapitalize: 'off'
		  },
		  showCancelButton: true,
		  confirmButtonText: 'Agregar',
		  cancelButtonText: 'Cancelar',
		  showLoaderOnConfirm: true,
		  preConfirm: (almacen) => {
			  if(document.getElementById("almacen").value.length<1 ||
					  document.getElementById("encargado").value.length<1 ||
					  document.getElementById("entreda").value.length<1 ||
					  document.getElementById("salida").value.length<1){
					Swal.showValidationMessage(
							`Complete todos los campos`
					)
				}
		  
		  },
		  allowOutsideClick: () => !Swal.isLoading()
		}).then((result) => {
		  if (result.value && document.getElementById("almacen").value ) {
				var almacen = document.getElementById("almacen").value;
				var encargado = document.getElementById("encargado").value;
				var entrada = document.getElementById("entreda").value;
				var salida = document.getElementById("salida").value;
				
			  $.ajax({
					type: "GET",
					url: "/verificar-duplicado-almacen",
					data: {
						'almacen': almacen,
						'encargado': encargado


					}

				}).done(function (data) {
					if(data==false){

						$.ajax({
							type: "POST",
							url: "/guardar-catalogo-almacen",
							data: {
								"_csrf": $('#token').val(),
								'almacen': almacen,
								'encargado':encargado,
								'entrada':entrada,
								'salida':salida
								

							}

						}).done(function (data) {
							listarClasificacion();
						});
						Swal.fire({
							position: 'center',
							icon: 'success',
							title: 'Insertado correctamente',
							showConfirmButton: false,
							timer: 1250
						})
						// / window.setTimeout(function(){location.reload()}, 2000);
					}// /fin segundoif
					else{
						Swal.fire({
							position: 'center',
							icon: 'error',
							title: 'registro duplicado no se ha insertado',
							showConfirmButton: false,
							timer: 1250
						})

					}
				});
		  }
		});
	

}

function listarClasificacion() {
	console.log ("jhola");
	$.ajax({
		method: "GET",
		url: "/listar-amp",
		data:{
			"Tipo":"Clasificacion"
		} ,
		success: (data) => {
			$('#quitar2').remove();
			$('#contenedorTabla2').append("<div class='modal-body' id='quitar2'>" +
					"<table class='table table-striped table-bordered' id='idtable2' style='width:100%'>" +
					"<thead>" +
					"<tr>" +
					"<th>Clave</th>" +
					"<th>Nomenclatura</th>" +
					
				
					"<th>Acciones</th>" +
					"</tr>" +
					"</thead>" +
					"</table>" + "</div>");
			var a;
			var b = [];
			if(rolAdmin==1){
				for (i in data) {
					var creacion =data[i].actualizadoPor==null?"":data[i].actualizadoPor;

					a = [
						"<tr>" +
						"<td>" + data[i].idText + "</td>",
						"<td>" + data[i].nombreLookup + "</td>",
						"<td style='text-align: center'>" +
						"<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>"+data[i].creadoPor+" <br /><strong>Fecha de creación:</strong> "+data[i].fechaCreacion+"<br><strong>Modificado por:</strong>"+creacion+"<br><strong>Fecha de modicación:</strong>"+data[i].ultimaFechaModificacion+"'><i class='fas fa-info'></i></button> " +
						" <button id='" + data[i].idLookup + "' value='" + data[i].nombreLookup + "' color='" + data[i].atributo1 + "' class='btn btn-warning btn-circle btn-sm popoverxd edit_data_color' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button> " +
						(data[i].estatus == 1 ? "<button onclick='bajarColor(" + data[i].idLookup + ")' class='btn btn-danger btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>" : " " ) +
						(data[i].estatus == 0 ? "<button onclick='reactivar(" + data[i].idLookup + ")' class='btn btn-success btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-sort-up'></i></button>" : " " ) +
						"</td>" +

						"<tr>"
						];
					b.push(a);
				}
			}
			else{
				for (i in data) {
					var creacion =data[i].actualizadoPor==null?"":data[i].actualizadoPor;
					if(data[i].estatus==1){
						a = [
							"<tr>" +
							"<td>" + data[i].idText + "</td>",
							"<td>" + data[i].nombreLookup + "</td>",
							"<td style='text-align: center'>" +
							"<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>"+data[i].creadoPor+" <br /><strong>Fecha de creación:</strong> "+data[i].fechaCreacion+"<br><strong>Modificado por:</strong>"+creacion+"<br><strong>Fecha de modicación:</strong>"+data[i].ultimaFechaModificacion+"'><i class='fas fa-info'></i></button> " +
							(rolEditar == 1 ? "<button id='" + data[i].idLookup + "' value='" + data[i].nombreLookup + "' color='" + data[i].atributo1 + "' class='btn btn-warning btn-circle btn-sm popoverxd edit_data_color' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>" : " ") +
							(rolEliminar == 1 ? "<button onclick='bajarColor(" + data[i].idLookup + ")' class='btn btn-danger btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>" : " ") +
							"</td>" +

							"<tr>"
							];
						b.push(a);

					}
				}
			}
			var tablaColores = $('#idtable2').DataTable({
				"data": b,
				"ordering": false,
				"pageLength": 5,
				"drawCallback": function() {
					$('.popoverxd').popover({
						container: 'body',
						trigger: 'hover'
					});
				},
				"responsive": true,
				"lengthMenu": [
					[5, 10, 25, 50, 100],
					[5, 10, 25, 50, 100]
					],
					"language": {
						"sProcessing": "Procesando...",
						"sLengthMenu": "Mostrar _MENU_ registros",
						"sZeroRecords": "No se encontraron resultados",
						"sEmptyTable": "Ningún dato disponible en esta tabla =(",
						"sInfo": "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
						"sInfoEmpty": "Mostrando registros del 0 al 0 de un total de 0 registros",
						"sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
						"sInfoPostFix": "",
						"sSearch": "Buscar:",
						"sUrl": "",
						"sInfoThousands": ",",
						"sLoadingRecords": "Cargando...",
						"oPaginate": {
							"sFirst": "Primero",
							"sLast": "Último",
							"sNext": "Siguiente",
							"sPrevious": "Anterior"
						},
						
						"buttons": {
							"copy": "Copiar",
							"colvis": "Visibilidad"
						}
					}
			});
			new $.fn.dataTable.FixedHeader(tablaColores);
		},
		error: (e) => {

		}
	})
}

function addMovimiento(){
	Swal.fire({
		  title: 'Nuevo Movimiento',
		  html:
			  '<div class="row">'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="descripcionMovimiento">Descripci&oacute;n</label>'+
				  	'<input type="text" class="form-control" id="descripcionMovimiento" placeholder="Trazo">'+
				  '</div>'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="origenMovimiento">Tipo de movimiento</label>'+
				  	'<select class="form-control" id="origenMovimiento">'+
				      '<option>Entrada</option>'+
				      '<option>Salida</option>'+
				   '</select>'+
				  '</div>'+
			  '</div>',
		  inputAttributes: {
		    autocapitalize: 'off'
		  },
		  showCancelButton: true,
		  confirmButtonText: 'Agregar',
		  cancelButtonText: 'Cancelar',
		  showLoaderOnConfirm: true,
		  preConfirm: (login) => {
		    return fetch(`//api.github.com/users/${login}`)
		      .then(response => {
		        if (!response.ok) {
		          throw new Error(response.statusText)
		        }
		        return response.json()
		      })
		      .catch(error => {
		        Swal.showValidationMessage(
		          `Request failed: ${error}`
		        )
		      })
		  },
		  allowOutsideClick: () => !Swal.isLoading()
		}).then((result) => {
		  if (result.value) {
			  Swal.fire({
				  position: 'center',
				  icon: 'success',
				  title: 'Movimiento agregado correctamente',
				  showConfirmButton: false,
				  timer: 2500
				})
		  }
		});
}
function editMovimiento(){
	Swal.fire({
		  title: 'Editar Movimiento',
		  html:
			  '<div class="row">'+
			  '<div class="form-group col-sm-6">'+
			  	'<label for="descripcionMovimientoEditar">Descripci&oacute;n</label>'+
			  	'<input type="text" class="form-control" id="descripcionMovimientoEditar" placeholder="Trazo">'+
			  '</div>'+
			  '<div class="form-group col-sm-6">'+
			  	'<label for="origenMovimientoEditar">Tipo de movimiento</label>'+
			  	'<select class="form-control" id="origenMovimientoEditar">'+
			      '<option>Entrada</option>'+
			      '<option>Salida</option>'+
			   '</select>'+
			  '</div>'+
		  '</div>',
		  inputAttributes: {
		    autocapitalize: 'off'
		  },
		  showCancelButton: true,
		  confirmButtonText: 'Agregar',
		  cancelButtonText: 'Cancelar',
		  showLoaderOnConfirm: true,
		  preConfirm: (login) => {
		    return fetch(`//api.github.com/users/${login}`)
		      .then(response => {
		        if (!response.ok) {
		          throw new Error(response.statusText)
		        }
		        return response.json()
		      })
		      .catch(error => {
		        Swal.showValidationMessage(
		          `Request failed: ${error}`
		        )
		      })
		  },
		  allowOutsideClick: () => !Swal.isLoading()
		}).then((result) => {
		  if (result.value) {
			  Swal.fire({
				  position: 'center',
				  icon: 'success',
				  title: 'Movimiento editado correctamente',
				  showConfirmButton: false,
				  timer: 2500
				})
		  }
		});
}

function deleteMovimiento(){
	Swal.fire({
		  title: '¿Deseas eliminar el movimiento?',
		  icon: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Confirmar'
		}).then((result) => {
		  if (result.value) {
			  Swal.fire({
				  position: 'center',
				  icon: 'success',
				  title: 'Movimiento eliminado correctamente',
				  showConfirmButton: false,
				  timer: 2500
				})
		  }
		});
}
function reactiveMovimiento(){
	Swal.fire({
		  title: '¿Deseas reactivar el movimiento?',
		  icon: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Confirmar'
		}).then((result) => {
		  if (result.value) {
			  Swal.fire({
				  position: 'center',
				  icon: 'success',
				  title: 'Movimiento reactivado correctamente',
				  showConfirmButton: false,
				  timer: 2500
				})
		  }
		});
}
// ALMACEN LOGICO
$('#detalleAlmacenesLogicos').on('shown.bs.modal', function () {
	$(document).off('focusin.modal');
});
function agregarAlmacenLogico(){
	Swal.fire({
		  title: 'Nuevo almac&eacute;n l&oacute;gico',
		  html:
			  '<div class="row">'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="nombreLogico">Nombre del almac&eacute;n l&oacute;gico</label>'+
				  	'<input type="text" class="form-control" id="nombreLogico" name="nombreLogico" placeholder="Almac&eacute;n 126">'+
				  '</div>'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="almacenFisicoLogico">Almac&eacute;n f&iacute;sico</label>'+
				  	'<select "'+almaceneslogicosselect()+'" class="form-control" id="almacenFisicoLogico" name="almacenFisicoLogico">'+
				  	'<option>seleccione un almacen</option>'+
				  	'</select>'+
				  '</div>'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="entradaLogico">Movimiento Entrada</label>'+
				  	'<select "'+selectentradas()+'" class="form-control" id="entradaLogico" name="entradaLogico">'+
				  	'<option>seleccione una entrada</option>'+
				  	'</select>'+
				  '</div>'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="salidaLogico">Movimiento Salida</label>'+
				  	'<select "'+selectsalidas()+'" class="form-control" id="salidaLogico" name="salidaLogico">'+
				  	'<option>seleccione una salida</option>'+
				  	'</select>'+
				  '</div>'+
			  '</div>',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Confirmar',
		  cancelButtonText: 'Cancelar'
		}).then((result) => {
		  if (result.value) {
			  if($('#nombreLogico').val().length>0 && $('#almacenFisicoLogico').val().length>0 && $('#salidaLogico').val().length>0 && $('#entradaLogico').val().length>0){
				  $.ajax({
                      type: "POST",
                      url: "/guardar-almacen-logico",
                      data: {
                          "_csrf": $('#token').val(),
                          'Nombre': $('#nombreLogico').val(),
                          'AlmacenFisico': $('#almacenFisicoLogico').val(),
                          'Salida': $('#salidaLogico').val(),
                        	  'Entrada': $('#entradaLogico').val(),
                          
                      }

                  }).done(function(data) {
                	  if(data==true){
                	  Swal.fire({
    					  position: 'center',
    					  icon: 'success',
    					  title: 'Ingresado correctamente',
    					  showConfirmButton: false,
    					  timer: 1500
    					})  }
                	  else{
                		  Swal.fire({
        					  position: 'center',
        					  icon: 'error',
        					  title: 'Registro duplicado algo ha salido mal reintente',
        					  showConfirmButton: false,
        					  timer: 1500
        					})  
                	  }
                	  //listarColores();
                	  listarAlmaceneslogicos();
                  });
		
			  }
			  else{
				  Swal.fire({
					  position: 'center',
					  icon: 'error',
					  title: 'Ingrese todos los campos requeridos',
					  showConfirmButton: false,
					  timer: 1500
					})  
				  
			  }
		  }
		});
}

function almaceneslogicosselect(){
	$.ajax({
	    type: "GET",
	    url: "/obtener-almacenes-fisicos-select",
	    success: (data) => {
	    	var select = document.getElementById("almacenFisicoLogico");
	    	//console.log(select);
	    	for(index in data) {
	    	    select.options[select.options.length] = new Option(data[index].nombreAlmacen, data[index].idAlmacenFisico);
	    	}
	    },
	    error: (e) => {
	    	
	    }
	});

	};
	
	function selectentradas(){
		$.ajax({
		    type: "GET",
		    url: "/entradas-salidas",
		    data:{
				"Tipo":"Entrada"
			} ,
		    success: (data) => {
		    	var select = document.getElementById("entradaLogico");
		    	//console.log(select);
		    	for(index in data) {
		    	    select.options[select.options.length] = new Option(data[index].nombreLookup, data[index].idLookup);
		    	}
		    },
		    error: (e) => {
		    	
		    }
		});

		};
		
		function selectsalidas(){
			$.ajax({
			    type: "GET",
			    url: "/entradas-salidas",
			    data:{
					"Tipo":"Salida"
				} ,
			    success: (data) => {
			    	var select = document.getElementById("salidaLogico");
			    	//console.log(select);
			    	for(index in data) {
			    	    select.options[select.options.length] = new Option(data[index].nombreLookup, data[index].idLookup);
			    	}
			    },
			    error: (e) => {
			    	
			    }
			});

			};


function listarAlmaceneslogicos() {

    $.ajax({
        method: "GET",
        url: "/get-all-amp-logico",
        success: (data) => {
            $('#quitar12').remove();
            $('#contenedorTabla12').append("<div class='modal-body' id='quitar12'>" +
                "<table class='table table-striped table-bordered' id='idtable12' style='width:100%'>" +
                "<thead>" +
                "<tr>" +
                "<th>Almacen Logico</th>" +
                "<th>Almacen fisico</th>" +
                "<th>Mov. Entrada</th>" +
                "<th>Mov. Salida</th>" +
                "<th>Acciones</th>" +
                "</tr>" +
                "</thead>" +
                "</table>" + "</div>");
            var a;
            var b = [];
           
                for (i in data) {
                    var creacion = data[i].actualizadoPor == null ? "" : data[i].actualizadoPor;
                    var idlogico=data[i][0];
                    var logico=data[i][1];
                    var idfisico=data[i][2];
                    var fisico=data[i][3];
                    var idsalida=data[i][4];
                    var salida=data[i][5];
                    var identrada=data[i][6];
                    var entrada=data[i][7];
                    a = [
                        "<tr>" +
                        "<td>" + data[i][1] + "</td>",
                        "<td>" + data[i][3] + "</td>",
                        "<td>" + data[i][7] + "</td>",
                        "<td>" + data[i][5] + "</td>",
                        "<td style='text-align: center;'>" +
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>" + data[i][8] + " <br /><strong>Fecha de creación:</strong> " + data[i][9] + "<br><strong>Modificado por:</strong>" + data[i][10] + "<br><strong>Fecha de modicación:</strong>" + data[i][11] + "'><i class='fas fa-info'></i></button> " +
                        "<button onclick='editarAlmacenLogico(\"" + idlogico+ "\",\"" + logico + "\",\"" + idfisico + "\",\"" + fisico + "\",\"" + idsalida + "\",\"" + salida + "\",\"" + identrada + "\",\"" + entrada + "\");'  class='btn btn-warning btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button> " +
                        (data[i][12] == 1 ? "<button onclick='bajaAlmacenLogico(" + idlogico + ")' class='btn btn-danger btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>" : " ") +
                        (data[i][12] == 0 ? "<button onclick='altaAlmacenLogico(" + idlogico + ")' class='btn btn-success btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-sort-up'></i></button>" : " ") +

                        "</td>" +
                        "<tr>"
                    ];
                    b.push(a);
                }
           
            var tablaMarcador = $('#idtable12').DataTable({
                "data": b,
                "ordering": false,
                "pageLength": 5,
                "responsive": true,
                "stateSave": true,
                "drawCallback": function() {
                    $('.popoverxd').popover({
                        container: 'body',
                        trigger: 'hover'
                    });
                },
                "columnDefs": [{
                        "type": "html",
                        "targets": '_all'
                    },
                    {
                        targets: 2,
                        className: 'dt-body-center'
                    }
                ],
                "lengthMenu": [
                    [5, 10, 25, 50, 100],
                    [5, 10, 25, 50, 100]
                ],
                "language": {
                    "sProcessing": "Procesando...",
                    "sLengthMenu": "Mostrar _MENU_ registros",
                    "sZeroRecords": "No se encontraron resultados",
                    "sEmptyTable": "Ningún dato disponible en esta tabla =(",
                    "sInfo": "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                    "sInfoEmpty": "Mostrando registros del 0 al 0 de un total de 0 registros",
                    "sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
                    "sInfoPostFix": "",
                    "sSearch": "Buscar:",
                    "sUrl": "",
                    "sInfoThousands": ",",
                    "sLoadingRecords": "Cargando...",
                    "oPaginate": {
                        "sFirst": "Primero",
                        "sLast": "Último",
                        "sNext": "Siguiente",
                        "sPrevious": "Anterior"
                    },
                    
                    "buttons": {
                        "copy": "Copiar",
                        "colvis": "Visibilidad"
                    }
                }
            });
            new $.fn.dataTable.FixedHeader(tablaMarcador);
        },
        error: (e) => {

        }
    })
}

function editarAlmacenLogico(
		idlogico,
        logico,
        idfisico,
        fisico,
        idsalida,
        salida,
        identrada,
        entrada
){
	Swal.fire({
		  title: 'Editar almac&eacute;n l&oacute;gico',
		  html:
			  '<div class="row">'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="nombreLogicoE">Nombre del almac&eacute;n l&oacute;gico</label>'+
				  	'<input value="'+logico+'" type="text" class="form-control" id="nombreLogicoE" name="nombreLogicoE" placeholder="Almac&eacute;n 126">'+
				  '</div>'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="almacenFisicoLogicoE">Almac&eacute;n f&iacute;sico</label>'+
				  	'<select "'+almacenfisicoeditarselect(idfisico)+'" class="form-control" id="almacenFisicoLogicoE" name="almacenFisicoLogicoE">'+
				  	'<option value="'+idfisico+'">'+fisico+'</option>'+
				  	'</select>'+
				  '</div>'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="entradaLogicoE">Movimiento Entrada</label>'+
				  	'<select "'+entradaSalidaSelectEditar(identrada,"Entrada")+'" class="form-control" id="entradaLogicoE" name="entradaLogicoE">'+
				  	'<option value="'+identrada+'">'+entrada+'</option>'+
				  	'</select>'+
				  '</div>'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="salidaLogicoE">Movimiento Salida</label>'+
				  	'<select "'+entradaSalidaSelectEditar(idsalida,"Salida")+'" class="form-control" id="salidaLogicoE" name="salidaLogicoE">'+
				  	'<option value="'+idsalida+'">'+salida+'</option>'+
				  	'</select>'+
				  '</div>'+
			  '</div>',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Confirmar',
		  cancelButtonText: 'Cancelar'
		}).then((result) => {
		  if (result.value) {
			  $.ajax({
					type: "POST",
					url: "/editar-almacen-logico",
					data: {
						"_csrf": $('#token').val(),
						'Id': idlogico,
						'AlmacenFisico': $('#almacenFisicoLogicoE').val(),
						'Nombre': $('#nombreLogicoE').val(),
						'Entrada': $('#entradaLogicoE').val(),
						'Salida':$('#salidaLogicoE').val()
					}

				}).done(function (data) {
					if(data==true){
						 Swal.fire({
							  position: 'center',
							  icon: 'success',
							  title: 'Almac&eacute;n l&oacute;gico editado correctamente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmaceneslogicos();
						
					}
					
					
					else{
						 Swal.fire({
							  position: 'center',
							  icon: 'error',
							  title: 'Registro duplicado algo ha salido mal reintente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmaceneslogicos();
					}
					
				});
		  }
		});
}

function almacenfisicoeditarselect(idfisico){
	$.ajax({
	    type: "GET",
	    url: "/obtener-almacenes-fisicos-select",
	    success: (data) => {
	    	var select = document.getElementById("almacenFisicoLogicoE");
	    	for (var i = 0; i < data.length; i++) {
				console.log(data[i]);
				if(data[i].idAlmacenFisico==idfisico){
					
					data.splice(i,1);
					
				}
			}
	    	for(index in data) {
	    	    select.options[select.options.length] = new Option(data[index].nombreAlmacen, data[index].idAlmacenFisico);
	    	}
	    },
	    error: (e) => {
	    	
	    }
	});

	};
	
	function entradaSalidaSelectEditar(identradasalida,Tipo){
		console.log(identradasalida);
		console.log(Tipo);
		$.ajax({
		    type: "GET",
		    url: "/entradas-salidas",
		    data: {
		    	
		    	'Tipo':Tipo
		    },
		    success: (data) => {
		    	console.log(data.tipoLookup=="Salida");
		    	console.log(data.tipoLookup=="Entrada");
		    	console.log(data.tipoLookup);
		    	var select;
		    	
		    	//var select = document.getElementById("almacenFisicoLogicoE");
		    	for (var i = 0; i < data.length; i++) {
					console.log(data[i]);
					if(data[i].idLookup==identradasalida){
						if (data[i].tipoLookup=="Salida") {
				    		console.log("salida");
				    		select = document.getElementById("salidaLogicoE");
						} else {
							console.log("entrada");
							select = document.getElementById("entradaLogicoE");
						}
						data.splice(i,1);
						
					}
				}
		    	for(index in data) {
		    	    select.options[select.options.length] = new Option(data[index].nombreLookup, data[index].idLookup);
		    	}
		    },
		    error: (e) => {
		    	
		    }
		});

		};
		
		
		
function bajaAlmacenLogico(id){
	Swal.fire({
		  title: '¿Deseas dar de baja el almac&eacute;n l&oacute;gico?',
		  icon: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Confirmar',
		  cancelButtonText: 'Cancelar'
		}).then((result) => {
		  if (result.value) {
			  $.ajax({
					type: "POST",
					url: "/baja-almacen",
					data: {
						"_csrf": $('#token').val(),
						'Id': id,
						'Tipo': 'Logico' 
					}

				}).done(function (data) {
					if(data==true){
						 Swal.fire({
							  position: 'center',
							  icon: 'success',
							  title: 'Almac&eacute;n l&oacute;gico dado de baja correctamente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmaceneslogicos();
						
					}
					
					
					else{
						 Swal.fire({
							  position: 'center',
							  icon: 'error',
							  title: 'Algo ha salido mal reintente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmaceneslogicos();
					}
					
				});
			  
		  }
		});
}
function altaAlmacenLogico(id){
	Swal.fire({
		  title: '¿Deseas dar de alta el almac&eacute;n l&oacute;gico?',
		  icon: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Confirmar',
		  cancelButtonText: 'Cancelar'
		}).then((result) => {
		  if (result.value) {
			  $.ajax({
					type: "POST",
					url: "/alta-almacen",
					data: {
						"_csrf": $('#token').val(),
						'Id': id,
						'Tipo': 'Logico' 
					}

				}).done(function (data) {
					if(data==true){
						 Swal.fire({
							  position: 'center',
							  icon: 'success',
							  title: 'Almac&eacute;n l&oacute;gico dado de alta correctamente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmaceneslogicos();
						
					}
					
					
					else{
						 Swal.fire({
							  position: 'center',
							  icon: 'error',
							  title: 'Algo ha salido mal reintente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmaceneslogicos();
					}
					
				});
			  
		  }
		});
}
// ALMACEN LOGICO
$('#detalleAlmacenesFisicos').on('shown.bs.modal', function () {
	$(document).off('focusin.modal');
});

//$( document ).ready(



//////////////agregar almacen fisico
function agregarAlmacenFisico(){
	Swal.fire({
		  title: 'Nuevo almac&eacute;n f&iacute;sico',
		  html:
			  '<div class="row">'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="nombreFisico">Nombre del almac&eacute;n f&iacute;sico</label>'+
				  	'<input type="text" class="form-control" id="nombreFisico" name="nombreFisico" placeholder="Almac&eacute;n 126">'+
				  '</div>'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="encargadoFisico">Encargado</label>'+
				  	'<select class="form-control" id="encargadoFisico" name="encargadoFisico">' +empleadosalmacen()+ '</select>'+
				  '</div>'+
			  '</div>',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Confirmar',
		  cancelButtonText: 'Cancelar'
		}).then((result) => {
			
		  if (result.value) {
			  if($('#nombreFisico').val().length>0 && $('#encargadoFisico').val().length>0){
				  $.ajax({
                      type: "POST",
                      url: "/guardar-almacen-fisico",
                      data: {
                          "_csrf": $('#token').val(),
                          'Nombre': $('#nombreFisico').val(),
                          'Encargado': $('#encargadoFisico').val()

                      }

                  }).done(function(data) {
                	  if(data==true){
                	  Swal.fire({
    					  position: 'center',
    					  icon: 'success',
    					  title: 'Ingresado correctamente',
    					  showConfirmButton: false,
    					  timer: 2500
    					})  }
                	  else{
                		  Swal.fire({
        					  position: 'center',
        					  icon: 'error',
        					  title: 'Registro duplicado o algo ha salido mal reintente',
        					  showConfirmButton: false,
        					  timer: 2500
        					})  
                	  }
                	  //listarColores();
                	  listarAlmacenesfisicos();
                  });
		
			  }
			  else{
				  Swal.fire({
					  position: 'center',
					  icon: 'error',
					  title: 'Ingrese todos los campos requeridos',
					  showConfirmButton: false,
					  timer: 1500
					})  
				  
			  }
			
		  }
		});
}
///////////////////////////////
/////////////listar

function listarAlmacenesfisicos() {

    $.ajax({
        method: "GET",
        url: "/get-all-amp-fisico",
        success: (data) => {
            $('#quitar11').remove();
            $('#contenedorTabla11').append("<div class='modal-body' id='quitar11'>" +
                "<table class='table table-striped table-bordered' id='idtable11' style='width:100%'>" +
                "<thead>" +
                "<tr>" +
                "<th>Almacen fisico</th>" +
                "<th>Encargado</th>" +
                "<th>Acciones</th>" +
                "</tr>" +
                "</thead>" +
                "</table>" + "</div>");
            var a;
            var b = [];
           
                for (i in data) {
                    //var creacion = data[i].actualizadoPor == null ? "" : data[i].actualizadoPor;
                    a = [
                        "<tr>" +
                        "<td>" + data[i][3] + "</td>",
                        "<td>" + data[i][0] + "</td>",
                        "<td style='text-align: center;'>" +
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>" + data[i][4] + " <br /><strong>Fecha de creación:</strong> " + data[i][6] + "<br><strong>Modificado por:</strong>" + data[i][5] + "<br><strong>Fecha de modicación:</strong>" + data[i][7] + "'><i class='fas fa-info'></i></button> " +
                        "<button onclick='editarAlmacenFisico(\"" + data[i][3] + "\",\"" + data[i][0] + "\",\"" + data[i][1] + "\",\"" + data[i][2] + "\");'  class='btn btn-warning btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button> " +
                        (data[i][8] == 1 ? "<button onclick='bajaAlmacenFisico(" + data[i][1] + ")' class='btn btn-danger btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>" : " ") +
                        (data[i][8] == 0 ? "<button onclick='altaAlmacenFisico(" + data[i][1] + ")' class='btn btn-success btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-sort-up'></i></button>" : " ") +
                        "<button onclick='abrirMapeo()' class='btn btn-altima btn-circle btn-sm popoverxd' data-content='Mapeo' data-placement='top'><i class='fas fa-map' style='margin-left: -1px;'></i></button>"+

                        "</td>" +
                        "<tr>"
                    ];
                    b.push(a);
                }
           
            var tablaMarcador = $('#idtable11').DataTable({
                "data": b,
                "ordering": false,
                "pageLength": 5,
                "responsive": true,
                "stateSave": true,
                "drawCallback": function() {
                    $('.popoverxd').popover({
                        container: 'body',
                        trigger: 'hover'
                    });
                },
                "columnDefs": [{
                        "type": "html",
                        "targets": '_all'
                    },
                    {
                        targets: 2,
                        className: 'dt-body-center'
                    }
                ],
                "lengthMenu": [
                    [5, 10, 25, 50, 100],
                    [5, 10, 25, 50, 100]
                ],
                "language": {
                    "sProcessing": "Procesando...",
                    "sLengthMenu": "Mostrar _MENU_ registros",
                    "sZeroRecords": "No se encontraron resultados",
                    "sEmptyTable": "Ningún dato disponible en esta tabla =(",
                    "sInfo": "Mostrando registros del _START_ al _END_ de un total de _TOTAL_ registros",
                    "sInfoEmpty": "Mostrando registros del 0 al 0 de un total de 0 registros",
                    "sInfoFiltered": "(filtrado de un total de _MAX_ registros)",
                    "sInfoPostFix": "",
                    "sSearch": "Buscar:",
                    "sUrl": "",
                    "sInfoThousands": ",",
                    "sLoadingRecords": "Cargando...",
                    "oPaginate": {
                        "sFirst": "Primero",
                        "sLast": "Último",
                        "sNext": "Siguiente",
                        "sPrevious": "Anterior"
                    },
                    
                    "buttons": {
                        "copy": "Copiar",
                        "colvis": "Visibilidad"
                    }
                }
            });
            new $.fn.dataTable.FixedHeader(tablaMarcador);
        },
        error: (e) => {

        }
    })
}
//////////////////////77acaba listar


function empleadosalmacen(){
	$.ajax({
	    type: "GET",
	    url: "/obtener-responsables-almacen",
	    success: (data) => {
	    	var select = document.getElementById("encargadoFisico");
	    	//console.log(select);
	    	for(index in data) {
	    	    select.options[select.options.length] = new Option(data[index][1], data[index][0]);
	    	}
	    },
	    error: (e) => {
	    	
	    }
	});

	};
function editarAlmacenFisico(almacen,encargado,id,idencargado){
	console.log(almacen);
	console.log(encargado);
	console.log(id);
	Swal.fire({
		  title: 'Editar almac&eacute;n f&iacute;sico',
		  html:
			  '<div class="row">'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="nombreFisicoE">Nombre del almac&eacute;n f&iacute;sico</label>'+
				  	'<input type="text" value="'+almacen+'" class="form-control" id="nombreFisicoE" name="nombreFisicoE" placeholder="Almac&eacute;n 126">'+
				  '</div>'+
				  '<div class="form-group col-sm-6">'+
				  	'<label for="encargadoFisicoE">Encargado</label>'+
				  	'<select "'+empleadosalmaceneditar(idencargado)+'" class="form-control" id="encargadoFisicoE" name="encargadoFisicoE">'+
				  	'<option value="'+idencargado+'">'+encargado+'</option>'+
				  	'</select>'+
				  '</div>'+
			  '</div>',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Confirmar',
		  cancelButtonText: 'Cancelar'
		}).then((result) => {
		  if (result.value) {
			  $.ajax({
					type: "POST",
					url: "/editar-almacen-fisico",
					data: {
						"_csrf": $('#token').val(),
						'Id': id,
						'Nombre': $('#nombreFisicoE').val(),
						'Encargado': $('#encargadoFisicoE').val()	
					}

				}).done(function (data) {
					if(data==true){
						 Swal.fire({
							  position: 'center',
							  icon: 'success',
							  title: 'Almac&eacute;n f&iacute;sico editado correctamente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmacenesfisicos();
						
					}
					
					
					else{
						 Swal.fire({
							  position: 'center',
							  icon: 'error',
							  title: 'Registro duplicado algo ha salido mal reintente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmacenesfisicos();
					}
					
				});
		  }
		});
}

function empleadosalmaceneditar(idencargado){
	$.ajax({
	    type: "GET",
	    url: "/obtener-responsables-almacen",
	    success: (data) => {
	    	var select = document.getElementById("encargadoFisicoE");
	    	for (var i = 0; i < data.length; i++) {
				console.log(data[i]);
				if(data[i][0]==idencargado){
					
					data.splice(i,1);
					
				}
			}
	    	for(index in data) {
	    	    select.options[select.options.length] = new Option(data[index][1], data[index][0]);
	    	}
	    },
	    error: (e) => {
	    	
	    }
	});

	};
function bajaAlmacenFisico(id){
	Swal.fire({
		  title: '¿Deseas dar de baja el almac&eacute;n f&iacute;sico?',
		  icon: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Confirmar',
		  cancelButtonText: 'Cancelar'
		}).then((result) => {
		  if (result.value) {
			  $.ajax({
					type: "POST",
					url: "/baja-almacen",
					data: {
						"_csrf": $('#token').val(),
						'Id': id,
						'Tipo': 'Fisico' 
					}

				}).done(function (data) {
					if(data==true){
						 Swal.fire({
							  position: 'center',
							  icon: 'success',
							  title: 'Almac&eacute;n f&iacute;sico dado de baja correctamente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmacenesfisicos();
						
					}
					
					
					else{
						 Swal.fire({
							  position: 'center',
							  icon: 'error',
							  title: 'Algo ha salido mal reintente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmacenesfisicos();
					}
					
				});
			  
			 
		  }
		});
}
function altaAlmacenFisico(id){
	Swal.fire({
		  title: '¿Deseas dar de alta el almac&eacute;n f&iacute;sico?',
		  icon: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Confirmar',
		  cancelButtonText: 'Cancelar'
		}).then((result) => {
		  if (result.value) {
			  $.ajax({
					type: "POST",
					url: "/alta-almacen",
					data: {
						"_csrf": $('#token').val(),
						'Id': id,
						'Tipo': 'Fisico' 
					}

				}).done(function (data) {
					if(data==true){
						 Swal.fire({
							  position: 'center',
							  icon: 'success',
							  title: 'Almac&eacute;n f&iacute;sico dado de alta correctamente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmacenesfisicos();
						
					}
					
					
					else{
						 Swal.fire({
							  position: 'center',
							  icon: 'error',
							  title: 'Algo ha salido mal reintente',
							  showConfirmButton: false,
							  timer: 2500
							});
						 listarAlmacenesfisicos();
					}
					
				});
			  
		  }
		});
}
function abrirMapeo(){
	$('#detalleMapeo').modal('show');
}