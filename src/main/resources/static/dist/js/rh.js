//Funci&oacute;n para agregar empresas
function agregarEmpresa() {
    Swal.fire({
        title: 'Nueva empresa',
        html: '<div class="row">' +
            '<div class="form-group col-sm-12">' +
            '<label for="empresa">Nombre de la empresa</label>' +
            '<input type="text" class="swal2-input" name="empresaLookup" id="empresaLookup" placeholder="Parisina">' +
            '<input type="hidden" id="idLookup" value="">' +
            '</div>' +
            '</div>',
        showCancelButton: true,
        cancelButtonColor: '#dc3545',
        cancelButtonText: 'Cancelar',
        confirmButtonText: 'Guardar',
        confirmButtonColor: '#0288d1',
        preConfirm: () => {
            if (document.getElementById("empresaLookup").value.length < 1) {
                Swal.showValidationMessage(
                    `Complete todos los campos`
                )
            }
        }
    }).then((result) => {
        if (result.value && document.getElementById("empresaLookup").value) {
            var nombreEmpresa = document.getElementById("empresaLookup").value;
            var idLookup = $('#idLookup').val();
            $.ajax({
                type: "GET",
                url: "/duplicadoEmpresa",
                data: {
                    "nombreEmpresa": nombreEmpresa,
                }
            }).done(function (data) {
                if (data == false) {
                    $.ajax({
                        type: "POST",
                        url: "/postEmpresa",
                        data: {
                            "_csrf": $('#token').val(),
                            "nombreEmpresa": nombreEmpresa,
                            "idLookup": idLookup
                        },
                        success: (data) => {
                            if (data == 1) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Empresa editada correctamente',
                                    showConfirmButton: false,
                                    timer: 2300,
                                })
                                $('#idLookup').val("");
                            } else if (data == 2) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Empresa agregada correctamente',
                                    showConfirmButton: false,
                                    timer: 2300,
                                })
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error',
                                    text: '&iexcl;Intente de nuevo!'
                                })
                            }
                            listarEmpresa();
                        }
                    })
                } else {
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
    })
}

//Habilitar input que se muestra deshabilitado
$('#empresaRH').on('shown.bs.modal', function () {
    $(document).off('focusin.modal');
});

//Listar empresas insertadas
function listarEmpresa() {

    $.ajax({
        method: "GET",
        url: "/getLookupHR",
        data: {
            "_csrf": $('#token').val(),
            "tipo": "Empresa"
        },
        success: (data) => {
            tableRHEmpresa.rows().remove();
            if (rolAdmin == 1) {
                for (i in data) {
                    tableRHEmpresa.row.add([
                        data[i]["idText"],
                        data[i]["nombreLookup"],
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        "<button class='btn btn-warning btn-circle btn-sm popoverxd' onclick='editarEmpresa(" + data[i]["idLookup"] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" +
                        (data[i].estatus == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' id=darBajaEmpresa onclick=darBajaEmpresa(" + data[i]["idLookup"] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i].estatus == 0 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' id=darAltaEmpresa onclick=darAltaEmpresa(" + data[i]["idLookup"] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>" : " ")
                    ]).draw(false);
                }
            } else
                for (i in data) {
                    tableRHEmpresa.row.add([
                        data[i]["idText"],
                        data[i]["nombreLookup"],
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        (rolEditar == 1 ? "<button class='btn btn-warning btn-circle btn-sm popoverxd' onclick='editarEmpresa(" + data[i]["idLookup"] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" : " ") +
                        (data[i].estatus == 1 && rolEliminar == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' id=darBajaEmpresa onclick=darBajaEmpresa(" + data[i]["idLookup"] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i].estatus == 0 && rolEliminar == 1 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' id=darAltaEmpresa onclick=darAltaEmpresa(" + data[i]["idLookup"] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>" : " ")
                    ]).draw(false);
                }
        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
}

//Funcion para dar de baja
function darBajaEmpresa(idLookup) {
    Swal.fire({
        title: '¿Deseas eliminar el proceso?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darBajaEmpresa",
                data: {
                    "_csrf": $('#token').val(),
                    "idLookup": idLookup
                },
                success: (data) => {
                    listarEmpresa();
                },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Dado de baja correctamente',
                showConfirmButton: false,
                timer: 2500
            })
        }
    });
}

//Funcion para dar de alta
function darAltaEmpresa(idLookup) {
    Swal.fire({
        title: '¿Deseas eliminar el proceso?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darAltaEmpresa",
                data: {
                    "_csrf": $('#token').val(),
                    "idLookup": idLookup
                },
                success: (data) => {
                    listarEmpresa()
                },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Dado de baja correctamente',
                showConfirmButton: false,
                timer: 2500
            })
        }
    });
}

//Funcion de editar empresa
function editarEmpresa(idLookup) {
    agregarEmpresa();
    $.ajax({
        method: "GET",
        url: "/editarEmpresa",
        data: {
            "_csrf": $('#token').val(),
            idLookup: idLookup
        },
        success: (data) => {
            $('#empresaLookup').val(data.nombreLookup);
            $('#idLookup').val(data.idLookup);
        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
}

//Funci&oacute;n para agregar &Aacute;reas
function agregarArea() {
    Swal.fire({
        title: 'Nueva &aacute;rea',
        html: '<div class="row">' +
            '<div class="form-group col-sm-12">' +
            '<label for="area">Nombre de la &aacute;rea</label>' +
            '<input type="text" class="swal2-input" id="areaLookup" placeholder="Producci&oacute;n">' +
            '<input type="hidden" id="idLookup" value="">' +
            '</div>' +
            '<input type="hidden" class="swal2-input" id="idarea">' +
            '</div>',
        showCancelButton: true,
        cancelButtonColor: '#dc3545',
        cancelButtonText: 'Cancelar',
        confirmButtonText: 'Guardar',
        confirmButtonColor: '#0288d1',
        preConfirm: () => {
            if (document.getElementById("areaLookup").value.length < 1) {
                Swal.showValidationMessage(
                    `Complete todos los campos`
                )
            }
        }
    }).then((result) => {
        if (result.value && document.getElementById("areaLookup").value) {
            var nombreArea = document.getElementById("areaLookup").value;
            var idLookup = $('#idLookup').val();
            $.ajax({
                type: "GET",
                url: "/duplicadoArea",
                data: {
                    "nombreArea": nombreArea,
                    "idLookup": idLookup
                }
            }).done(function (data) {
                if (data == false) {
                    $.ajax({
                        type: "POST",
                        url: "/postArea",
                        data: {
                            "_csrf": $('#token').val(),
                            "nombreArea": nombreArea,
                            "idLookup": idLookup
                        },
                        success: (data) => {
                            console.log(data);
                            if (data == 1) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: '&Aacute;rea editada correctamente',
                                    showConfirmButton: false,
                                    timer: 2300,
                                })
                                $('#idLookup').val("");
                            } else if (data == 2) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: '&Aacute;rea agregada correctamente',
                                    showConfirmButton: false,
                                    timer: 2300,
                                })
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error',
                                    text: '&iexcl;Intente de nuevo!'
                                })
                            }
                            listarAreas();
                        }
                    })
                } else {
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
    })
}

//Habilitar input que se muestra deshabilitado
$('#areaRH').on('shown.bs.modal', function () {
    $(document).off('focusin.modal');
});

//Listar &Aacute;reas insertadas
function listarAreas() {
    $.ajax({
        method: "GET",
        url: "/getLookupHR",
        data: {
            "_csrf": $('#token').val(),
            "tipo": "Area"
        },
        success: (data) => {
            tableRHArea.rows().remove().draw();
            if (rolAdmin == 1) {
                for (i in data) {
                    tableRHArea.row.add([
                        data[i]["idText"],
                        data[i]["nombreLookup"],
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        "<button class='btn btn-warning btn-circle btn-sm popoverxd'  onclick='editarArea(" + data[i]["idLookup"] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" +
                        (data[i].estatus == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' id=darBajaArea onclick=darBajaArea(" + data[i]["idLookup"] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i].estatus == 0 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' id=darAltaArea onclick=darAltaArea(" + data[i]["idLookup"] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>" : " ")
                    ]).draw(false);
                }
            } else
                for (i in data) {
                    tableRHArea.row.add([
                        data[i]["idText"],
                        data[i]["nombreLookup"],
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        (rolEditar == 1 ? "<button class='btn btn-warning btn-circle btn-sm popoverxd'  onclick='editarArea(" + data[i]["idLookup"] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" : " ") +
                        (data[i].estatus == 1 && rolEliminar == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' id=darBajaArea onclick=darBajaArea(" + data[i]["idLookup"] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i].estatus == 0 && rolEliminar == 1 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' id=darAltaArea onclick=darAltaArea(" + data[i]["idLookup"] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>" : " ")
                    ]).draw(false);
                }

        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
}

//Funcion para dar de baja
function darBajaArea(idLookup) {
    Swal.fire({
        title: '¿Deseas eliminar el proceso?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darBajaArea",
                data: {
                    "_csrf": $('#token').val(),
                    "idLookup": idLookup
                },
                success: (data) => {
                    Swal.fire({
                        position: 'center',
                        icon: 'success',
                        title: 'Dado de baja correctamente',
                        showConfirmButton: false,
                        timer: 2500
                    })
                    listarAreas();
                },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });

        }
    });
}

//Funcion para dar de alta
function darAltaArea(idLookup) {
    Swal.fire({
        title: '¿Deseas eliminar el proceso?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darAltaArea",
                data: {
                    "_csrf": $('#token').val(),
                    "idLookup": idLookup
                },
                success: (data) => {
                    Swal.fire({
                        position: 'center',
                        icon: 'success',
                        title: 'Dado de baja correctamente',
                        showConfirmButton: false,
                        timer: 2500
                    })
                    listarAreas();
                },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });

        }
    });
}

//Funcion de editar Area
function editarArea(idLookup) {

    $.ajax({
        method: "GET",
        url: "/editarArea",
        data: {
            "_csrf": $('#token').val(),
            idLookup: idLookup
        },
        success: (data) => {
            $('#areaLookup').val(data.nombreLookup);
            $('#idLookup').val(data.idLookup);
        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
    agregarArea();
}

//Funci&oacute;n para agregar departamento
function agregarDepartamento(idArea) {
    console.log(idArea)
    mostrarAreas(idArea);
    Swal.fire({
        title: 'Nuevo departamento',
        html: '<div class="row">' +
            '<div class="form-group col-sm-12">' +
            '<label for="departamento">Nombre del departamento</label>' +
            '<input type="text" class="swal2-input" id="departamento" placeholder="Ingresa el departamento">' +
            '</div>' +
            '</div>' +
            '<label for="areadep">&Aacute;rea</label>' +
            '<select class="form-control selectpicker" title="Seleccione uno..." id="listarAreasCatalogos" data-live-search="true"></select>' +
            '<input type="hidden" id="idDepartamento" value="">' +
            '</div>',
        showCancelButton: true,
        cancelButtonColor: '#6C757D',
        cancelButtonText: 'Cancelar',
        confirmButtonText: 'Guardar',
        confirmButtonColor: '#28a745',
        preConfirm: () => {
            if (document.getElementById("departamento").value.length < 1 || document.getElementById("listarAreasCatalogos").value.length < 1) {
                Swal.showValidationMessage(
                    `Complete todos los campos`
                )
            }
        }
    }).then((result) => {
        if (result.value && document.getElementById("departamento").value) {
            var nombreDepartamento = document.getElementById("departamento").value;
            var nomArea = document.getElementById("listarAreasCatalogos").value;
            var Departamento = $('#idDepartamento').val();
            $.ajax({
                type: "GET",
                url: "/duplicadoDepartamento",
                data: {
                    "nombreDepartamento": nombreDepartamento,
                    "nomArea": nomArea
                }
            }).done(function (data) {
                if (data == false) {
                    $.ajax({
                        type: "POST",
                        url: "/postDepartamento",
                        data: {
                            "_csrf": $('#token').val(),
                            "idDepartamento": Departamento,
                            "nombreDepartamento": nombreDepartamento,
                            "nomArea": nomArea
                        },
                        success: (data) => {
                            console.log(data);
                            if (data == 1) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Departamento editado correctamente',
                                    showConfirmButton: false,
                                    timer: 2300,

                                })
                                $('#idDepartamento').val("");
                            } else if (data == 2) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Departamento     agregado correctamente',
                                    showConfirmButton: false,
                                    timer: 2300,

                                })
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error',
                                    text: 'Registro duplicado!'
                                })
                            }
                            listarDepartamentos()
                        },
                        error: function (data) {
                            Swal.close();
                            Swal.fire({
                                icon: 'error',
                                title: 'Error',
                                text: 'Registro duplicado!',
                            })
                        }
                    });
                }
            })
        }
    })

}

//Habilitar input que se muestra deshabilitado
$('#departamentosRH').on('shown.bs.modal', function () {
    $(document).off('focusin.modal');
});

//funci&oacute;n para mostrar &aacute;reas en departamentos
function mostrarAreas(idArea) {
    console.log("que pepe")
    $.ajax({
        method: "GET",
        url: "/rh-listarAreas",
        data: {
            "_csrf": $('#token').val(),
            "tipo": "Area"
        },
        success: (data) => {
            console.log(idArea + "nmz");
            for (var key in data) {
                $('#listarAreasCatalogos').append('<option value="' + data[key]["idLookup"] + '">' + data[key]["nombreLookup"] + '</option>')
            }
            $('#listarAreasCatalogos').selectpicker('val', idArea);
            $('#listarAreasCatalogos').selectpicker('refresh');
        },
        error: (e) => {
            location.reload();
        }
    });
}

//Listar departamentos insertados
function listarDepartamentos() {
    $.ajax({
        method: "GET",
        url: "/getListarDepartamentos",
        data: {
            "_csrf": $('#token').val()
        },
        success: (data) => {
            tableRHDepartamento.rows().remove().draw();
            if (rolAdmin == 1) {
                for (i in data) {
                    tableRHDepartamento.row.add([
                        data[i][5],
                        data[i][1],
                        data[i][3],
                        data[i][7],
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        "<button class='btn btn-warning btn-circle btn-sm popoverxd' onclick='editarDepartamento(" + data[i][0] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" +
                        (data[i][4] == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' onclick=darBajaDepartamento(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i][4] == 0 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' onclick=darAltaDepartamento(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>&nbsp;" : " ") +
                        `<button class="btn btn-secondary btn-circle btn-sm popoverxd" onclick="asignarCoordinador(${data[i][0]},${data[i][6]})" data-container="body" data-toggle="popover" data-placement="top" data-content="Asignar Coordinador"><i class="fas fa-user-cog"></i></button>`
                    ]).draw(false);
                }
            } else
                for (i in data) {
                    tableRHDepartamento.row.add([
                        data[i][5],
                        data[i][1],
                        data[i][3],
                        data[i][7],
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        (rolEditar == 1 ? "<button class='btn btn-warning btn-circle btn-sm popoverxd' onclick='editarDepartamento(" + data[i][0] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" : " ") +
                        (data[i][4] == 1 && rolEliminar == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' onclick=darBajaDepartamento(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i][4] == 0 && rolEliminar == 1 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' onclick=darAltaDepartamento(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>" : " ") +
                        `<button class="btn btn-secondary btn-circle btn-sm popoverxd" onclick="asignarCoordinador(${data[i][0]},${data[i][6]})" data-container="body" data-toggle="popover" data-placement="top" data-content="Asignar Coordinador"><i class="fas fa-user-cog"></i></button>`
                    ]).draw(false);
                }
        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
}

//Funcion para dar de baja
function darBajaDepartamento(idLookup) {
    Swal.fire({
        title: '¿Deseas dar de baja el departamento?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darBajaDepartamento",
                data: {
                    "_csrf": $('#token').val(),
                    "idLookup": idLookup
                },
                success: (data) => {
                    listarDepartamentos();
                },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Dado de baja correctamente',
                showConfirmButton: false,
                timer: 2500
            })
        }
    });
}

//Funcion para dar de alta
function darAltaDepartamento(idLookup) {
    Swal.fire({
        title: '¿Deseas dar de alta el departamento?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darAltaDepartamento",
                data: {
                    "_csrf": $('#token').val(),
                    "idLookup": idLookup
                },
                success: (data) => {
                    listarDepartamentos();
                },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Dado de baja correctamente',
                showConfirmButton: false,
                timer: 2500
            })
        }
    });
}

//Funcion de editar departamento
function editarDepartamento(idLookup) {
    $.ajax({
        method: "GET",
        url: "/editarDepartamento",
        data: {
            "_csrf": $('#token').val(),
            idLookup: idLookup
        },
        success: (data) => {
            console.log(data)
            agregarDepartamento(data[2]);
            $('#departamento').val(data[1]);
            $('#idDepartamento').val(data[0]);
        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
}

async function asignarCoordinador(id, coordinador) {
    $("#modalAsignarCoordinador").modal("show");
    $("#idDepartamento").val(id);
    try {
        let result = await $.ajax({
            url: "/getEmpleadosSelect",
            type: 'GET',
        });
        console.log(result)
        result.map(empleado => {
            $('#coordinador').append(`<option value="${empleado[0]}">${empleado[1]}</option>`)
        })
        $('#coordinador').selectpicker('val', coordinador);
        $('#coordinador').selectpicker('refresh');
    } catch (error) {
        alert(error);
    }
}
async function guardarCoordinador() {
    let idDepartamento = $("#idDepartamento").val();
    let coordinador = $("#coordinador").val();
    if (idDepartamento.trim() === '' ||
        coordinador.trim() === '') {
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Todos los campos son requeridos!'
        })
        return false
    }
    try {
        await $.ajax({
            url: `/putEmpleadosSelect/${idDepartamento}`,
            type: 'PUT',
            data: {
                coordinador: coordinador,
                _csrf: $("[name='_csrf']").val()
            }
        });
        Swal.fire({
            position: 'center',
            icon: 'success',
            title: 'El coordinador se guardo correctamente!',
            showConfirmButton: false,
            timer: 2500
        })
        listarDepartamentos();
    } catch (error) {
        alert(error);
    }
    console.log({ idDepartamento, coordinador })
}

//Funci&oacute;n para agregar puestos
function agregarPuesto(idDepartamento) {
    mostrarDepartamentos(idDepartamento);
    Swal.fire({
        title: 'Agregar Puesto',
        html: '<form method="POST" enctype="multipart/form-data" id="fileUploadForm" class="row">' +
            '<div class="form-group col-md-6">' +
            '<label for="puesto">Nombre Puesto</label>' +
            '<input type="text" class="form-control" id="nombrePuesto" name="nombrePuesto" placeholder="Coordinador">' +
            '</div>' +
            '<div class="form-group col-md-6">' +
            '<label for="exampleFormControlSelect1">Departamento</label>' +
            '<select class="form-control" id="departamento" name="departamento">' +
            '</select>' +
            '</div>' +
            '<div class="form-group col-md-6">' +
            '<label for="puesto">Plaza</label>' +
            '<input type="number" class="form-control" id="nomPlazas" name="nomPlazas" placeholder="Coordinador">' +
            '</div>' +
            '<div class="form-group col-md-6">' +
            '<label for="puesto">Sueldo</label>' +
            '<input type="number" step="any" class="form-control" id="sueldos" name="sueldos" placeholder="100">' +
            '</div>' +
            '<div class="form-group col-md-6">' +
            '<label for="puesto">Perfil</label>' +
            '<input type="file" class="form-control" value="false" name="perfiles" id="perfiles">' +
            '</div>' +
            '<div class="form-group col-md-6">' +
            '<label for="horarioExtra">Genera tiempo extra</label>' +
            '<input type="checkbox" class="form-control" value="false" name="checkbox" id="checkbox" onclick="$(this).val(this.checked ? true : false)">' +
            '</div>' +
            '<input type="hidden" id="idPuesto" name="idPuesto" value="">' +
            '</form>',
        showCancelButton: true,
        cancelButtonColor: '#6C757D',
        cancelButtonText: 'Cancelar',
        confirmButtonText: 'Guardar',
        confirmButtonColor: '#28a745',
        preConfirm: (color) => {
            if (document.getElementById("nombrePuesto").value.length < 1 || document.getElementById("departamento").value.length < 1 ||
                document.getElementById("checkbox").value.length < 1 || document.getElementById("nomPlazas").value.length < 1 ||
                document.getElementById("sueldos").value.length < 1 || document.getElementById("perfiles").value.length < 1) {
                Swal.showValidationMessage(
                    `Complete todos los campos`
                )
            }
        }
    }).then((result) => {
        if (result.value) {
            var form = $('#fileUploadForm')[0];
            var formdata = new FormData(form);
            formdata.append("_csrf",$('#token').val())
            $.ajax({
                type: "POST",
                enctype: 'multipart/form-data',
                url: "/postPuesto",
                data: formdata,
                processData: false,
                contentType: false,
                cache: false,
                timeout: 600000,
                success: (data) => {
                    console.log(data)
                    if (data === 1) {
                        $('#closePuestos').click();
                        Swal.fire({
                            position: 'center',
                            icon: 'success',
                            title: 'Puesto editado correctamente',
                            showConfirmButton: false,
                            timer: 2300,
                            onClose: () => {
                                $('#btnPuestosDetalle').click();
                            }
                        })
                        $('#idPuesto').val("");
                    } else if (data === 2) {
                        Swal.fire({
                            position: 'center',
                            icon: 'success',
                            title: 'Puesto agregado correctamente',
                            showConfirmButton: false,
                            timer: 2300,
                            onClose: () => {
                                $('#btnPuestosDetalle').click();
                            }
                        })
                    } else if(data===3) {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'Registro Duplicado!'
                        })
                    }
                }
            }).done(function (data) {
                ListarPuestos();
            });


        }
    })
}

//Habilitar input que se muestra deshabilitado
$('#puestoRH').on('shown.bs.modal', function () {
    $(document).off('focusin.modal');
});

//Funci&oacute;n para mostrar departamentos en puestos
function mostrarDepartamentos(idDepartamento) {
    $.ajax({
        method: "GET",
        url: "/getMostrarDepartamentos",
        data: {
            "_csrf": $('#token').val(),
        },
        success: (data) => {
            console.log(data);
            for (var key in data) {
                $('#departamento').append('<option value="' + data[key]["idDepartamento"] + '">' + data[key]["nombreDepartamento"] + '</option>')
            }
            if (idDepartamento != null) {
                $('#departamento option[value="' + idDepartamento + '"]').attr("selected", true);
            }
        },
        error: (e) => {
            location.reload();
        }
    });
}

//Listar puestos insertados
function ListarPuestos() {
    $.ajax({
        method: "GET",
        url: "/getListarPuestos",
        data: {
            "_csrf": $('#token').val()
        },
        success: (data) => {
            tableRHPuestos.rows().remove().draw();
            if (rolAdmin == 1) {
                for (i in data) {
                    console.log(data[i])
                    tableRHPuestos.row.add([
                        data[i][9],
                        data[i][1],
                        data[i][3],
                        data[i][4] == 1 ? "Aceptado" : "Rechazado",
                        data[i][5],
                        data[i][6],
                        data[i][7],
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        "<button class='btn btn-warning btn-circle btn-sm popoverxd' onclick='editarPuesto(" + data[i][0] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" +
                        (data[i][8] == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' id=darBajaPuesto onclick=darBajaPuesto(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i][8] == 0 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' id=darAltaPuesto onclick=darAltaPuesto(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>" : " ")+
                        `<a class="btn btn-secondary btn-circle btn-sm popoverxd" data-container="body" data-toggle="popover" data-placement="top" data-content="Descargar archivo" href="https://res.cloudinary.com/dti-consultores/image/upload/v1621531227/rh/${data[i][10]}" target="_blank"><i class="fas fa-file-download"></i></a>`
                    ]).draw(false);
                }
            } else
                for (i in data) {
                    tableRHPuestos.row.add([
                        data[i][9],
                        data[i][1],
                        data[i][3],
                        data[i][4] == 1 ? "Aceptado" : "Rechazado",
                        data[i][5],
                        data[i][6],
                        data[i][7],
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        (rolEditar == 1 ? "<button class='btn btn-warning btn-circle btn-sm popoverxd' onclick='editarPuesto(" + data[i][0] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" : " ") +
                        (data[i][8] == 1 && rolEliminar == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' id=darBajaPuesto onclick=darBajaPuesto(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i][8] == 0 && rolEliminar == 1 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' id=darAltaPuesto onclick=darAltaPuesto(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>" : " ")+
                        `<a class="btn btn-secondary btn-circle btn-sm popoverxd" data-container="body" data-toggle="popover" data-placement="top" data-content="Descargar archivo" href="https://res.cloudinary.com/dti-consultores/image/upload/v1621531227/rh/${data[i][10]}" target="_blank"><i class="fas fa-file-download"></i></a>`
                    ]).draw(false);
                }
        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
}

//Funcion para dar de baja
function darBajaPuesto(idLookup) {
    Swal.fire({
        title: '¿Deseas eliminar el proceso?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darBajaPuesto",
                data: {
                    "_csrf": $('#token').val(),
                    "idLookup": idLookup
                },
                success: (data) => { },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Dado de baja correctamente',
                showConfirmButton: false,
                timer: 2500
            })
        }
    });
}

//Funcion para dar de alta
function darAltaPuesto(idLookup) {
    Swal.fire({
        title: '¿Deseas eliminar el proceso?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darAltaPuesto",
                data: {
                    "_csrf": $('#token').val(),
                    "idLookup": idLookup
                },
                success: (data) => { },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Dado de baja correctamente',
                showConfirmButton: false,
                timer: 2500
            })
        }
    });
}

//Funcion de editar puestos
function editarPuesto(idLookup) {
    $.ajax({
        method: "GET",
        url: "/editarPuesto",
        data: {
            "_csrf": $('#token').val(),
            idLookup: idLookup
        },
        success: (data) => {
            console.log(data)
            agregarPuesto(data[2], data[4]);
            $('#puesto').val(data[1]);
            $('#idPuesto').val(data[0]);
            $('#horarioExtra').val(data[4]);
            $("#horarioExtra[value='true']").attr("checked", "checked")
            $('#plaza').val(data[5]);
            $('#sueldo').val(data[6]);
            $('#perfil').val(data[7]);
            $("#perfil[value='true']").attr("checked", "checked")
        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
}

//funcion para agregar horarios
function agregarHorario() {
    Swal.fire({
        title: 'Agregar Horario',
        html: '<div class="row">' +
            '<label class="col-md-6" for="horarioinicio">Inicio Horario</label>' +
            '<label class="col-md-6" for="horariosalida">Salida Horario</label>' +
            '<input type="time" class="swal2-input col-md-5" id="horarioinicio">' +
            '<input type="time" class="swal2-input col-md-5 offset-md-2" id="horariosalida">' +
            '</div>' +
            '<div class="row">' +
            '<label class="col-md-6" for="horacomidainicio">Inicio Comida</label>' +
            '<label class="col-md-6" for="horacomidafin">Final Comida</label>' +
            '<input type="time" class="swal2-input col-md-5" id="horacomidainicio">' +
            '<input type="time" class="swal2-input col-md-5 offset-md-2" id="horacomidafin">' +
            '</div>' +
            '<input type="hidden" class="swal2-input" id="idHorario">' +
            '</div>',
        showCancelButton: true,
        cancelButtonColor: '#6C757D',
        cancelButtonText: 'Cancelar',
        confirmButtonText: 'Guardar',
        confirmButtonColor: '#28a745',
        preConfirm: () => {
            if (document.getElementById("horarioinicio").value.length < 1 ||
                document.getElementById("horariosalida").value.length < 1 ||
                document.getElementById("horacomidainicio").value.length < 1 ||
                document.getElementById("horacomidafin").value.length < 1) {
                Swal.showValidationMessage(
                    `Complete todos los campos`
                )
            }
        }
    }).then((result) => {
        if (result.value && document.getElementById("horarioinicio").value) {
            var idHorario = document.getElementById("idHorario").value;
            var horaInicio = document.getElementById("horarioinicio").value;
            var horaSalida = document.getElementById("horariosalida").value;
            var horaComida = document.getElementById("horacomidainicio").value;
            var horaRegresoComida = document.getElementById("horacomidafin").value;
            $.ajax({
                type: "GET",
                url: "/duplicadoHorario",
                data: {
                    "horaInicio": horaInicio,
                    "horaSalida": horaSalida,
                    "horaComida": horaComida,
                    "horaRegresoComida": horaRegresoComida
                }
            }).done(function (data) {
                if (data == false) {
                    $.ajax({
                        type: "POST",
                        url: "/postHorarioLaboral",
                        data: {
                            "_csrf": $('#token').val(),
                            "idHorario": idHorario,
                            "horaInicio": horaInicio,
                            "horaSalida": horaSalida,
                            "horaComida": horaComida,
                            "horaRegresoComida": horaRegresoComida
                        },
                        success: (data) => {
                            if (data == 1) {
                                $('#closeHorario').click();
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Horario editado correctamente',
                                    showConfirmButton: false,
                                    timer: 2300,
                                    onClose: () => {
                                        $('#btnHorariosDetalle').click();
                                    }
                                })
                                $('#idHorario').val("");
                            } else if (data = 2) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Horario agregado correctamente',
                                    showConfirmButton: false,
                                    timer: 2300,
                                    onClose: () => {
                                        $('#btnHorariosDetalle').click();
                                    }
                                })
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error',
                                    text: '&iexcl;Intente de nuevo!'
                                })
                            }
                        }
                    }).done(function (data) {
                        ListarHorarios();
                    });
                } else {
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
    })
}

//Listar horarios insertados
function ListarHorarios() {
    $.ajax({
        method: "GET",
        url: "/getListarHorarios",
        data: {
            "_csrf": $('#token').val()
        },
        success: (data) => {
            tableRHHorarios.rows().remove().draw();
            if (rolAdmin == 1) {
                for (i in data) {
                    tableRHHorarios.row.add([
                        data[i][10],
                        data[i][1],
                        data[i][2],
                        data[i][3],
                        data[i][4],
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        "<button class='btn btn-warning btn-circle btn-sm popoverxd' onclick='editarHorario(" + data[i][0] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" +
                        (data[i][9] == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' id=darBajaHorario onclick=darBajaHorario(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i][9] == 0 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' id=darAltaHorario onclick=darAltaHorario(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>" : " ")
                    ]).draw(false);
                }
            } else
                for (i in data) {
                    tableRHHorarios.row.add([
                        data[i][10],
                        data[i][1],
                        data[i][2],
                        data[i][3],
                        data[i][4],
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        (rolEditar == 1 ? "<button class='btn btn-warning btn-circle btn-sm popoverxd' onclick='editarHorario(" + data[i][0] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" : " ") +
                        (data[i][9] == 1 && rolEliminar == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' id=darBajaHorario onclick=darBajaHorario(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i][9] == 0 && rolEliminar == 1 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' id=darAltaHorario onclick=darAltaHorario(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>" : " ")
                    ]).draw(false);
                }
        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
}

//Funcion para dar de baja
function darBajaHorario(idLookup) {
    Swal.fire({
        title: '¿Deseas eliminar el proceso?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darBajaHorario",
                data: {
                    "_csrf": $('#token').val(),
                    "idLookup": idLookup
                },
                success: (data) => { },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Dado de baja correctamente',
                showConfirmButton: false,
                timer: 2500
            })
        }
    });
}

//Funcion para dar de alta
function darAltaHorario(idLookup) {
    Swal.fire({
        title: '¿Deseas eliminar el proceso?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darBajaHorario",
                data: {
                    "_csrf": $('#token').val(),
                    "idLookup": idLookup
                },
                success: (data) => { },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Dado de baja correctamente',
                showConfirmButton: false,
                timer: 2500
            })
        }
    });
}

//Funcion de editar horario
function editarHorario(idHorario) {
    agregarHorario();
    $.ajax({
        method: "GET",
        url: "/editarHorario",
        data: {
            "_csrf": $('#token').val(),
            idHorario: idHorario
        },
        success: (data) => {
            $('#idHorario').val(data[0][0]);
            $('#horarioinicio').val(data[0][1]);
            $('#horariosalida').val(data[0][2]);
            $('#horacomidainicio').val(data[0][3]);
            $('#horacomidafin').val(data[0][4]);
        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
}

//Funci&oacute;n para agregar calendarios
function agregarCalendario() {
    Swal.fire({
        title: 'Agregar Calendario',
        html: '<div class="row">' +
            '<div class="form-group col-sm-12">' +
            '<label for="diafestivo">Fecha</label>' +
            '<input type="date" class="swal2-input" id="diafestivo">' +
            '</div>' +
            '</div>' +
            '<div class="row">' +
            '<div class="form-group col-sm-12">' +
            '<label for="nombrefecha">Nombre</label>' +
            '<input type="text" class="swal2-input" id="nombrefecha" placeholder="Dia de la independencia">' +
            '</div>' +
            '</div>' +
            '<div class="row">' +
            '<div class="form-group col-sm-12">' +
            '<label for="estatusfecha">Status</label>' +
            '<input type="checkbox" class="swal2-input" value="0"  id="checkbox" onclick="$(this).val(this.checked ? 1 : 0)">' +
            '</div>' +
            '</div>' +
            '<input type="hidden" class="swal2-input" id="idCalendario" value>' +
            '</div>',
        showCancelButton: true,
        cancelButtonColor: '#6C757D',
        cancelButtonText: 'Cancelar',
        confirmButtonText: 'Guardar',
        confirmButtonColor: '#28a745',
        preConfirm: (color) => {
            if (document.getElementById("diafestivo").value.length < 1 || document.getElementById("nombrefecha").value.length < 1 || document.getElementById("checkbox").value.length < 1) {
                Swal.showValidationMessage(
                    `Complete todos los campos`
                )
            }
        }
    }).then((result) => {
        if (result.value && document.getElementById("diafestivo").value) {
            var idCalendario = document.getElementById("idCalendario").value;
            var fechaFestivo = document.getElementById("diafestivo").value;
            var festividad = document.getElementById("nombrefecha").value;
            var estatusFestivo = document.getElementById("checkbox").value;
            $.ajax({
                type: "GET",
                url: "/duplicadoCalendario",
                data: {
                    "fechaFestivo": fechaFestivo,
                    "festividad": festividad,
                    "estatusFestivo": estatusFestivo
                }
            }).done(function (data) {
                if (data == false) {
                    $.ajax({
                        type: "POST",
                        url: "/postCalendarios",
                        data: {
                            "_csrf": $('#token').val(),
                            "idCalendario": idCalendario,
                            "fechaFestivo": fechaFestivo,
                            "festividad": festividad,
                            "estatusFestivo": estatusFestivo
                        },
                        success: (data) => {
                            if (data == 1) {
                                $('#closeCalendario').click();
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Calendario editado correctamente',
                                    showConfirmButton: false,
                                    timer: 2300,
                                    onClose: () => {
                                        $('#btnCalendarioDetalle').click();
                                    }
                                })
                                $('#idCalendario').val("");
                            } else if (data = 2) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Calendario agregado correctamente',
                                    showConfirmButton: false,
                                    timer: 2300,
                                    onClose: () => {
                                        $('#btnCalendarioDetalle').click();
                                    }
                                })
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Error',
                                    text: '&iexcl;Intente de nuevo!'
                                })
                            }
                        }
                    }).done(function (data) {
                        ListarCalendarios();
                    });
                } else {
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
    })
}

//Habilitar input que se muestra deshabilitado
$('#detalleCuidado').on('shown.bs.modal', function () {
    $(document).off('focusin.modal');
});

//Listar calendarios insertados
function ListarCalendarios() {
    $.ajax({
        method: "GET",
        url: "/getListarCalendarios",
        data: {
            "_csrf": $('#token').val()
        },
        success: (data) => {
            tableRHCalendario.rows().remove().draw();
            if (rolAdmin == 1) {
                for (i in data) {
                    tableRHCalendario.row.add([
                        data[i][8],
                        data[i][1],
                        data[i][2],
                        data[i][7] == 1 ? "Valido" : "No valido",
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        "<button class='btn btn-warning btn-circle btn-sm popoverxd' onclick='editarCalendario(" + data[i][0] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" +
                        (data[i][7] == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' onclick=darBajaCalendario(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i][7] == 0 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' onclick=darAltaCalendario(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>" : " ")
                    ]).draw(false);
                }
            } else
                for (i in data) {
                    tableRHCalendario.row.add([
                        data[i][8],
                        data[i][1],
                        data[i][2],
                        data[i][7] == 1 ? "Valido" : "No valido",
                        "<button class='btn btn-info btn-circle btn-sm popoverxd' data-container='body' data-toggle='popover' data-placement='top' data-html='true' data-content='<strong>Creado por: </strong>Manuel Perez <br /><strong>Fecha de creaci&oacute;n: </strong>20/12/2019<br><strong>Modificado por: </strong>Jose luis<br><strong>Fecha de modicaci&oacute;n: </strong>21/02/2020'><i class='fas fa-info'></i></button>&nbsp;" +
                        (rolEditar == 1 ? "<button class='btn btn-warning btn-circle btn-sm popoverxd' onclick='editarCalendario(" + data[i][0] + ")' data-container='body' data-toggle='popover' data-placement='top' data-content='Editar'><i class='fas fa-pen'></i></button>&nbsp;" : " ") +
                        (data[i][7] == 1 && rolEliminar == 1 ? "<button class='btn btn-danger btn_remove btn-circle btn-sm popoverxd' onclick=darBajaCalendario(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Dar de baja'><i class='fas fa-caret-down'></i></button>&nbsp;" : " ") +
                        (data[i][7] == 0 && rolEliminar == 1 ? "<button class='btn btn-success btn-circle btn-sm popoverxd' onclick=darAltaCalendario(" + data[i][0] + ") data-container='body' data-toggle='popover' data-placement='top' data-content='Reactivar'><i class='fas fa-caret-up'></i></button>" : " ")
                    ]).draw(false);
                }
        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
}

//Funcion para dar de baja
function darBajaCalendario(idCalendario) {
    Swal.fire({
        title: '¿Deseas eliminar el proceso?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darBajaCalendario",
                data: {
                    "_csrf": $('#token').val(),
                    "idCalendario": idCalendario
                },
                success: (data) => { },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Dado de baja correctamente',
                showConfirmButton: false,
                timer: 2500
            })
        }
    });
}

//Funcion para dar de alta
function darAltaCalendario(idCalendario) {
    Swal.fire({
        title: '¿Deseas eliminar el proceso?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Confirmar'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: "GET",
                url: "/darAltaCalendario",
                data: {
                    "_csrf": $('#token').val(),
                    "idCalendario": idCalendario
                },
                success: (data) => { },
                error: function (data) {
                    alert("Error en el servidor");
                }
            });
            Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'Dado de baja correctamente',
                showConfirmButton: false,
                timer: 2500
            })
        }
    });
}

//Funcion de editar calendareio
function editarCalendario(idCalendario) {
    agregarCalendario();
    $.ajax({
        method: "GET",
        url: "/editarCalendario",
        data: {
            "_csrf": $('#token').val(),
            idCalendario: idCalendario
        },
        success: (data) => {
            $('#idCalendario').val(data.idCalendario);
            $('#diafestivo').val(data.fecha);
            $('#nombrefecha').val(data.nombreCalendario);
            $('#checkbox').val(data.estatus);
            $("#checkbox[value='true']").attr("checked", "checked")
            console.log(data.idCalendario);
            console.log(data.fecha);
            console.log(data.nombreCalendario);
            console.log(data.estatus);
        },
        error: (e) => {
            alert("Error en el servidor");
        }
    });
}