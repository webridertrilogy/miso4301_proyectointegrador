/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.naming.NamingException;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.Cargo;
import co.uniandes.sisinfo.entities.Contacto;
import co.uniandes.sisinfo.entities.InscripcionEventoExterno;
import co.uniandes.sisinfo.entities.Pregunta;
import co.uniandes.sisinfo.entities.Respuesta;
import co.uniandes.sisinfo.entities.SectorCorporativo;
import co.uniandes.sisinfo.entities.UsuarioEventos;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.Departamento;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.Pais;
import co.uniandes.sisinfo.entities.datosmaestros.soporte.TipoDocumento;
import co.uniandes.sisinfo.serviciosfuncionales.CargoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.PreguntaFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.SectorCorporativoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.DepartamentoFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.PaisFacadeRemote;
import co.uniandes.sisinfo.serviciosfuncionales.soporte.TipoDocumentoFacadeRemote;

/**
 *
 * @author Administrador
 */
public class ConversorContacto {

    @EJB
    private SectorCorporativoFacadeLocal sectorFacade;
    @EJB
    private PaisFacadeRemote paisFacade;
    @EJB
    private ConstanteRemote constanteBean;
    @EJB
    private CargoFacadeLocal cargoFacade;
    @EJB
    private DepartamentoFacadeRemote departamentoFacade;
    @EJB
    private PreguntaFacadeLocal preguntaFacade;
    private ServiceLocator serviceLocator;
    private TipoDocumentoFacadeRemote tipoDocumentoFacade;

    public ConversorContacto(CargoFacadeLocal cargoFacadeLo, PaisFacadeRemote paisFacadeRe, SectorCorporativoFacadeLocal sectorFacadeLo, TipoDocumentoFacadeRemote tipoDocumentoFacadeRe, DepartamentoFacadeRemote departamentoFacadeRe, PreguntaFacadeLocal preguntaFacadeLo) {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
            cargoFacade = cargoFacadeLo;
            paisFacade = paisFacadeRe;
            sectorFacade = sectorFacadeLo;
            tipoDocumentoFacade = tipoDocumentoFacadeRe;
            departamentoFacade = departamentoFacadeRe;
            preguntaFacade = preguntaFacadeLo;
        } catch (NamingException ex) {
            Logger.getLogger(ConversorContacto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }

    public Secuencia getInfoContactos(Collection<Contacto> contactos) {
        Secuencia secContactos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTOS), "");
        for (Contacto contacto : contactos) {
            secContactos.agregarSecuencia(getInfoContacto(contacto));
        }
        return secContactos;
    }

    /**
     * Retorna una secuencia con la información básica de los contactos (id, nombre, apellidos y correo)
     * La información restante de cada contacto no se incluye dentro de la secuencia
     * @param contactos Lista de contactos a parsear
     * @return Lista de secuencias con la informacion del contacto
     */
    public Secuencia getInfoContactosLight(Collection<Contacto> contactos) {
        Secuencia secContactos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTOS), "");
        for (Contacto contacto : contactos) {
            secContactos.agregarSecuencia(getInfoContacto(contacto));
        }
        return secContactos;
    }

    /**
     * Retorna una secuencia con la información básica del contacto (id, nombre apellidos y correo).
     * La información restante del contacto no se incluye dentro de la secuencia
     * @param c Contacto a parsear
     * @return Secuencia con la información resumida del contacto
     */
    public Secuencia getInfoContactoLight(Contacto c) {
        Secuencia secInfoContacto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTO), "");
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CONTACTO), c.getId().toString()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), c.getNombres()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), c.getApellidos()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), c.getCorreo()));

        return secInfoContacto;
    }

    public Secuencia getInfoContacto(Contacto c) {
        Secuencia secInfoContacto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CONTACTO), "");
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CONTACTO), "" + c.getId()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), c.getNombres()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), c.getApellidos()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EMPRESA), c.getEmpresa()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION), c.getDireccion()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CIUDAD), c.getCiudad()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INDICATIVO), c.getIndicativo()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TELEFONO), c.getTelefono()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FAX), c.getFax()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CELULAR), c.getCelular()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), c.getCorreo()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO), c.getCorreoAlterno()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OTROS), c.getOtroCargo()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DIRECCION_WEB), c.getPaginaWeb()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_OBSERVACIONES), c.getObservaciones()));
        secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EXTENSION), c.getExtension()));


        //DateFormat sdf = DateFormat.getDateTimeInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (c.getFechaActualizacion() != null) {
            secInfoContacto.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ACTUALIZACION), sdf.format(c.getFechaActualizacion())));
        }
        //tablaHaciaSecuencia.put("FechaActualizacion", getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_ACTUALIZACION));

        //Date fechaHoy = new Date(System.currentTimeMillis());
        //c.setFechaActualizacion(fechaHoy);
        if (c.getNumeroIdentificacion() != null) {
            Secuencia secNumerodeIdentificacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DOCUMENTO), c.getNumeroIdentificacion());
            secInfoContacto.agregarSecuencia(secNumerodeIdentificacion);
        }
        if (c.getPais() != null) {
            Secuencia secPais = consultarPais(c.getPais());
            secInfoContacto.agregarSecuencia(secPais);
        }
        if (c.getCargo() != null) {
            Secuencia secCargo = consultarCargo(c.getCargo());
            secInfoContacto.agregarSecuencia(secCargo);
        }
        if (c.getSector() != null) {
            Secuencia secSectorCorporativo = consultarSectorCorporativo(c.getSector());
            secInfoContacto.agregarSecuencia(secSectorCorporativo);
        }
        if (c.getTipoDocumento() != null) {
            Secuencia secTipoDocumento = consultarTipoDocumento(c.getTipoDocumento());
            secInfoContacto.agregarSecuencia(secTipoDocumento);
        }
        if (c.getDepartamento() != null) {
            Secuencia secDepartamento = consultarDepartamento(c.getDepartamento());
            secInfoContacto.agregarSecuencia(secDepartamento);
        }
        return secInfoContacto;
    }

    public Secuencia consultarDepartamento(Departamento departamento) {

        Secuencia secDepartamento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DEPARTAMENTO), "");
        if (departamento.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), departamento.getId().toString());
            secDepartamento.agregarSecuencia(secId);
        }
        if (departamento.getNombre() != null) {
            Secuencia secTipoDoc = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), departamento.getNombre());
            secDepartamento.agregarSecuencia(secTipoDoc);
        }
        return secDepartamento;
    }

    public Secuencia consultarTipoDocumento(TipoDocumento tipoDocumento) {

        Secuencia secTipoDocumento = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO), "");

        if (tipoDocumento.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tipoDocumento.getId().toString());
            secTipoDocumento.agregarSecuencia(secId);
        }

        if (tipoDocumento.getTipo() != null) {
            Secuencia secTipoDoc = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), tipoDocumento.getTipo());
            secTipoDocumento.agregarSecuencia(secTipoDoc);
        }

        if (tipoDocumento.getDescripcion() != null) {
            Secuencia secDescripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESC_TIPO_DOCUMENTO), tipoDocumento.getDescripcion());
            secTipoDocumento.agregarSecuencia(secDescripcion);
        }
        return secTipoDocumento;
    }

    public Secuencia consultarCargo(Cargo cargo) {
        Secuencia secCargo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_CARGO), "");
        if (cargo.getNombre() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), cargo.getNombre());
            secCargo.agregarSecuencia(secNombre);
        }
        if (cargo.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), cargo.getId().toString());
            secCargo.agregarSecuencia(secId);
        }
        return secCargo;
    }

    public Secuencia consultarSectoresCorporativos(Collection<SectorCorporativo> sectores) {

        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECTORES_CORPORATIVOS), "");

        for (SectorCorporativo sec : sectores) {
            Secuencia secSector = consultarSectorCorporativo(sec);
            secPrincipal.agregarSecuencia(secSector);
        }

        return secPrincipal;
    }

    public Secuencia consultarSectorCorporativo(SectorCorporativo sector) {

        Secuencia secSector = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SECTOR_CORPORATIVO), "");
        if (sector.getNombre() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), sector.getNombre().toString());
            secSector.agregarSecuencia(secNombre);
        }
        if (sector.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), sector.getId().toString());
            secSector.agregarSecuencia(secId);
        }

        return secSector;
    }

    public Secuencia consultarPais(Pais pais) {

        Secuencia secPais = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PAIS), "");

        if (pais.getNombre() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), pais.getNombre().toString());
            secPais.agregarSecuencia(secNombre);
        }
        if (pais.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), pais.getId().toString());
            secPais.agregarSecuencia(secId);
        }

        return secPais;
    }

    public Contacto getInfoContacto(Secuencia i) throws ParseException {

        Contacto c = new Contacto();
        Secuencia secId = i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_CONTACTO));
        if (secId != null) {
            Long id = Long.parseLong(secId.getValor().trim());
            c.setId(id);
        }
        c.setNombres(i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_NOMBRES)).getValor());
        c.setApellidos(i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_APELLIDOS)).getValor());

        c.setOtroCargo((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_OTROS)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_OTROS)).getValor() : null);

        c.setEmpresa(i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_EMPRESA)).getValor());

        c.setDireccion((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DIRECCION)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DIRECCION)).getValor() : null);

        c.setCiudad((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CIUDAD)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CIUDAD)).getValor() : null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //c.setFechaActualizacion(new java.sql.Date(System.currentTimeMillis()));

        c.setIndicativo((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_INDICATIVO)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_INDICATIVO)).getValor() : null);
        c.setTelefono((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TELEFONO)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TELEFONO)).getValor() : null);
        c.setCelular((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CELULAR)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CELULAR)).getValor() : null);
        c.setCorreo(i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO)).getValor());
        c.setCorreoAlterno((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO_ALTERNO)).getValor() : null);

        Secuencia secCargo = i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CARGO));
        if (secCargo != null) {
            Long idCargo = Long.parseLong(secCargo.getValor());
            Cargo cargo = cargoFacade.find(idCargo);
            c.setCargo(cargo);
        }

        Secuencia secSector = i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_SECTOR_CORPORATIVO));
        if (secSector != null) {
            Long idSector = Long.parseLong(secSector.getValor());
            SectorCorporativo sector = sectorFacade.find(idSector);
            c.setSector(sector);
        }

        Secuencia secDepartamento = i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DEPARTAMENTO));
        if (secDepartamento != null) {
            Long idDepartamento = Long.parseLong(secDepartamento.getValor());
            Departamento departamento = departamentoFacade.find(idDepartamento);
            c.setDepartamento(departamento);
        }


        c.setPaginaWeb((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DIRECCION_WEB)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DIRECCION_WEB)).getValor() : null);

        c.setObservaciones((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_OBSERVACIONES)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_OBSERVACIONES)).getValor() : null);

        c.setNumeroIdentificacion((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DOCUMENTO)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_DOCUMENTO)).getValor() : null);

        String idTipoDoc = (i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_TIPO_DOCUMENTO)).getValor() : null;
        if (idTipoDoc != null) {
            TipoDocumento documento = tipoDocumentoFacade.find(Long.parseLong(idTipoDoc));
            c.setTipoDocumento(documento);
        }


        String pais = (i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PAIS)) != null)
                ? (i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_PAIS)).getValor()) : null;
        if (pais != null) {
            Pais p = paisFacade.findByNombre(pais);
            if (p != null) {
                c.setPais(p);
            }
        }

        c.setExtension((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_EXTENSION)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_EXTENSION)).getValor() : null);
        c.setFax((i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FAX)) != null)
                ? i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_FAX)).getValor() : null);
        return c;
    }

    public UsuarioEventos getUsuarioEventoExterno(Secuencia i) {

        UsuarioEventos usuario = new UsuarioEventos();
        Secuencia secId = i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secId != null) {
            Long id = Long.parseLong(secId.getValor().trim());
            usuario.setId(id);
        }
        usuario.setCorreo(i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO)).getValor());
        if (i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CONTRASENHA)) != null) {
            usuario.setContrasena(i.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CONTRASENHA)).getValor());
        }
        return usuario;

    }

    public Secuencia getUsuarioEvento(UsuarioEventos usuario) {

        Secuencia secUsuarioEvento = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_USUARIO), "");

        Long id = usuario.getId();
        String correo = usuario.getCorreo();
        Secuencia secId = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_GENERAL), "" + id);
        secUsuarioEvento.agregarSecuencia(secId);
        Secuencia secNombre = new Secuencia(constanteBean.getConstante(Constantes.TAG_PARAM_CORREO), correo);
        secUsuarioEvento.agregarSecuencia(secNombre);
        //System.out.println("Usuario: " +usuario.get);
        secUsuarioEvento.agregarSecuencia(getInfoContacto(usuario.getContacto()));

        return secUsuarioEvento;
    }

    public Collection<Pregunta> consultarPreguntas(Secuencia secPreguntas) {
        Collection<Pregunta> preguntas = new ArrayList<Pregunta>();
        Collection<Secuencia> secuenciaPreguntas = secPreguntas.getSecuencias();
        for (Secuencia sec : secuenciaPreguntas) {
            preguntas.add(consultarPregunta(sec));
        }
        return preguntas;
    }

    public Pregunta consultarPregunta(Secuencia secPregunta) {
        Pregunta pregunta = new Pregunta();
        Secuencia secId = secPregunta.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PREGUNTA));
        if (secId != null) {
            pregunta.setId(Long.parseLong(secId.getValor()));
        }
        Secuencia secValor = secPregunta.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR_PREGUNTA));
        if (secValor != null) {
            pregunta.setPregunta(secValor.getValor());
        }
        return pregunta;
    }



    public Collection<Respuesta> consultarRespuestas(Secuencia secRespuestas) {
        Collection<Respuesta> respuestas = new ArrayList<Respuesta>();
        Collection<Secuencia> secuenciaRespuestas = secRespuestas.getSecuencias();
        for (Secuencia sec : secuenciaRespuestas) {

            respuestas.add(consultarRespuesta(sec));
        }
        return respuestas;
    }
    
    public Secuencia consultarPreguntas(Collection<Pregunta> preguntas) {

        Secuencia secPreguntas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PREGUNTAS), "");
        for (Pregunta pregunta : preguntas) {
            secPreguntas.agregarSecuencia(consultarPreguntaVO(pregunta));
        }

        return secPreguntas;
    }

    public Secuencia consultarPreguntaVO(Pregunta pregunta) {
        Secuencia secPregunta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PREGUNTA), "");

        if (pregunta.getId() != null) {
            Secuencia secIdPreg = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PREGUNTA), pregunta.getId().toString());
            secPregunta.agregarSecuencia(secIdPreg);
        }

        if (pregunta.getPregunta() != null) {
            Secuencia secPreg = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR_PREGUNTA), pregunta.getPregunta());
            secPregunta.agregarSecuencia(secPreg);
        }
        return secPregunta;
    }



    public Respuesta consultarRespuesta(Secuencia secRespuesta) {
        Respuesta respuesta = new Respuesta();
        Secuencia secId = secRespuesta.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_RESPUESTA));
        if (secId != null) {
            respuesta.setId(Long.parseLong(secId.getValor()));
        }
        Secuencia secValor = secRespuesta.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_VALOR_RESPUESTA));
        if (secValor != null) {
            respuesta.setRespuesta(secValor.getValor());
        }

        Secuencia secIdPregunta = secRespuesta.obtenerPrimeraSecuencia(constanteBean.getConstante(Constantes.TAG_PARAM_ID_PREGUNTA));
        if (secIdPregunta != null) {
            Long id = Long.parseLong(secIdPregunta.getValor());
            Pregunta p = preguntaFacade.findById(id);
            if (p != null) {
                respuesta.setPregunta(p);
            }
        }
        return respuesta;
    }

    public Secuencia consultarRespuestas (Collection<Respuesta> respuestas){
        Secuencia secRespuestas = new Secuencia (getConstanteBean().getConstante(Constantes.TAG_PARAM_RESPUESTAS),"");
        for (Respuesta respuesta : respuestas) {
            secRespuestas.agregarSecuencia(consultarRespuesta(respuesta));
        }
        return secRespuestas;
    }

    public Secuencia consultarRespuesta (Respuesta respuesta){
        Secuencia secRespuesta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RESPUESTA), "");
        if (respuesta.getId() != null) {
            Secuencia secIdResp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_RESPUESTA), respuesta.getId().toString());
            secRespuesta.agregarSecuencia(secIdResp);
        }

        if (respuesta.getRespuesta() != null) {
            Secuencia secResp = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR_RESPUESTA), respuesta.getRespuesta());
            secRespuesta.agregarSecuencia(secResp);
        }

        if (respuesta.getPregunta() != null) {
            Secuencia secPreg = consultarPreguntaVO(respuesta.getPregunta());
            secRespuesta.agregarSecuencia(secPreg);
        }

        return secRespuesta;
    }

    public Secuencia consultarInscripciones(Collection<InscripcionEventoExterno> inscripciones) {
        Secuencia secInscripciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EVENTO_INSCRIPCIONES), "");
        for (InscripcionEventoExterno inscripcion : inscripciones) {
            Secuencia secInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EVENTO_INSCRIPCION), "");
            secInscripcion.agregarSecuencia(new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EVENTO_INSCRIPCION_ID), inscripcion.getId()+""));
            secInscripcion.agregarSecuencia(getInfoContacto(inscripcion.getContacto()));
            secInscripcion.agregarSecuencia(consultarRespuestas(inscripcion.getRespuestas()));
            secInscripciones.agregarSecuencia(secInscripcion);
        }
        return secInscripciones;
    }
}
