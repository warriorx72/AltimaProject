$(document).ready(function () {



});

function articulo(id,tipo){
	var id2=id;
	var tipo2=tipo;
	$('#articulo').val(id2); 
	$('#tipo').val(tipo2); 
}

function agregarmultialmacen(){
	var articulo=$('#articulo').val();
	var tipo=$('#tipo').val();
	if($('#almacenLogicoInventario').val().length>0 && $('#tipo').val().length>0 && $('#articulo').val().length>0){
		$.ajax({
			type: "POST",
			url: "/guardar-multialmacen",
			data: {
				"_csrf": $('#token').val(),
				'AlmacenLogico':$('#almacenLogicoInventario').val(),
				'Tipo': tipo,
				'Articulo': articulo


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
			// listarColores();
			tablamulti(articulo,tipo);
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



};


function tablamulti(articulo,tipo){
	var tipoparam;
	if(tipo=="aa"){
		tipoparam="materialAlmacen";

	}else if(tipo=="t"){
		tipoparam="tela";

	}else if(tipo=="f"){
		tipoparam="forro";

	}
	else if(tipo=="m"){

		tipoparam="material";
	}

	$.ajax({
		method: "GET",
		url: "/multialmacen-articulos",
		data: {

			'articulo':articulo,
			'tipo':tipoparam
		},
		success: (data) => {
			$('#quitarmultialmacenes').remove();
			$('#multialmacenes').append("<div class='modal-body' id='quitarmultialmacenes'>" +
					"<table class='table table-striped table-bordered' id='idtablemultialmacenes' style='width:100%'>" +
					"<thead>" +
					"<tr>" +
					"<th>Almacén lógico</th>" +
					"<th></th>" +
					"</tr>" +
					"</thead>" +
					"</table>" + "</div>");
			var a;
			var b = [];

			for (i in data) {
				// var creacion = data[i].actualizadoPor == null ? "" :
				// data[i].actualizadoPor;
				a = [
					"<tr>" +
					"<td>" + data[i][1] + "</td>",
					"<td>" +
					"<button onclick='EliminarMultialmacen(" + data[i][0] + ")' class='btn btn-danger btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-content='Eliminar'><i class='fas fa-minus-circle'></i></button>"   +
					"</td>" +
					"<tr>"
					];
				b.push(a);
			}

			var tablaMultialmacenes = $('#idtablemultialmacenes').DataTable({
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
					targets: 1,
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
			new $.fn.dataTable.FixedHeader(tablaMultialmacenes);
		},
		error: (e) => {

		}
	})

}

function EliminarMultialmacen(idmultialmacen){
	var articulo= $('#articulo').val();
	var tipo= $('#tipo').val();


	$.ajax({
		type: "DELETE",
		url: "/eliminar-multialmacen",
		data: {
			"_csrf": $('#token').val(),
			'idmultialmacen':idmultialmacen


		}

	}).done(function(data) {
		if(data==true){
			Swal.fire({
				position: 'center',
				icon: 'success',
				title: 'Eliminado correctamente',
				showConfirmButton: false,
				timer: 1500
			})  }
		else{
			Swal.fire({
				position: 'center',
				icon: 'error',
				title: 'No se ha podido eliminar algo ha salido mal reintente',
				showConfirmButton: false,
				timer: 1500
			})  
		}
		// listarColores();
		tablamulti(articulo,tipo);

	})}

