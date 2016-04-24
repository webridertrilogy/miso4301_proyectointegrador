/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.serviciosnegocio;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.entities.CampoAdicional;
import co.uniandes.sisinfo.entities.CategoriaEventoExterno;
import co.uniandes.sisinfo.entities.EventoExterno;
import co.uniandes.sisinfo.entities.Pregunta;
import co.uniandes.sisinfo.entities.TipoCampo;
import co.uniandes.sisinfo.serviciosfuncionales.CategoriaEventoExternoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.EventoExternoFacadeLocal;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;

/**
 *
 * @author Administrador
 */
public class ConversorEventoExterno {

    private ConstanteLocal constanteBean;
    private SimpleDateFormat formatoFechaHora;
    private EventoExternoFacadeLocal eventoExternoFacade;
    private CategoriaEventoExternoFacadeLocal categoriaFacade;
    private SimpleDateFormat formatoFecha;

    public ConversorEventoExterno(ConstanteLocal constanteBean, EventoExternoFacadeLocal eventoExternoFacade, CategoriaEventoExternoFacadeLocal categoriaFacade) {
        this.constanteBean = constanteBean;
        this.eventoExternoFacade = eventoExternoFacade;
        this.categoriaFacade = categoriaFacade;
        formatoFechaHora = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
    }

    public ConstanteLocal getConstanteBean() {
        return constanteBean;
    }

    public Secuencia consultarCategorias(Collection<CategoriaEventoExterno> categoriasEventoExterno) {
        Secuencia secCategorias = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIAS_EVENTO_EXTERNO), "");
        for (CategoriaEventoExterno categoria : categoriasEventoExterno) {
            secCategorias.agregarSecuencia(consultarCategoria(categoria));
        }
        return secCategorias;
    }

    public Secuencia consultarCategoria(CategoriaEventoExterno categoria) {

        Secuencia secCategoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIA_EVENTO_EXTERNO), "");

        if (categoria.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CATEGORIA), categoria.getId().toString());
            secCategoria.agregarSecuencia(secId);
        }

        if (categoria.getNombre() != null) {
            Secuencia secCargo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CATEGORIA), categoria.getNombre());
            secCategoria.agregarSecuencia(secCargo);
        }
        return secCategoria;
    }

    public Secuencia consultarEventosExternos(Collection<EventoExterno> eventosExternos) {
        Secuencia secEventos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EVENTOS_EXTERNOS), "");
        for (EventoExterno eventoExterno : eventosExternos) {
            secEventos.agregarSecuencia(consultarEventoExterno(eventoExterno));
        }
        return secEventos;
    }

    public Secuencia consultarEventoExterno(EventoExterno evento) {

        Secuencia secEventoExterno = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_EVENTO_EXTERNO), "");

        if (evento.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO), evento.getId().toString());
            secEventoExterno.agregarSecuencia(secId);
        }

        if (evento.getTitulo() != null) {
            Secuencia secCargo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO), evento.getTitulo());
            secEventoExterno.agregarSecuencia(secCargo);
        }
        if (evento.getDescripcion() != null) {
            Secuencia secDescripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION), evento.getDescripcion());
            secEventoExterno.agregarSecuencia(secDescripcion);
        }
        if (evento.getCupo() != null) {
            Secuencia secCupo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUPO), evento.getCupo().toString());
            secEventoExterno.agregarSecuencia(secCupo);
        }

        if (evento.getCategoria() != null) {
            Secuencia secCategoria = consultarcategoria(evento.getCategoria());
            secEventoExterno.agregarSecuencia(secCategoria);
        }

        if (evento.getFechaHoraInicio() != null) {
            Secuencia secFechaHoraInicio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_HORA), formatoFechaHora.format(evento.getFechaHoraInicio()));
            secEventoExterno.agregarSecuencia(secFechaHoraInicio);
        }


        if (evento.getFechaLimiteInscripciones() != null) {
            Secuencia secFechaLimiteInscripciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_LIMITE_INSCRIPCIONES), formatoFechaHora.format(evento.getFechaLimiteInscripciones()));
            secEventoExterno.agregarSecuencia(secFechaLimiteInscripciones);
        }

        if (evento.getRutaImagen() != null) {
            Secuencia secRutaImagen = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_IMAGEN), evento.getRutaImagen());
            secEventoExterno.agregarSecuencia(secRutaImagen);
        }

        if (evento.getEstado() != null) {
            Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO), evento.getEstado());
            secEventoExterno.agregarSecuencia(secEstado);
        }

        if (evento.getPreguntas() != null) {
            Secuencia preguntas = consultarPreguntas(evento.getPreguntas());
            secEventoExterno.agregarSecuencia(preguntas);
        }
        if (evento.getCamposAdicionales() != null) {
            Secuencia campos = consultarCamposAdicionales(evento.getCamposAdicionales());
            secEventoExterno.agregarSecuencia(campos);
        }
        if (evento.getLinkInscripcion() != null) {
            Secuencia secLinkInscripcion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_LINK_INSCRIPCION_EVENTO), evento.getLinkInscripcion());
            secEventoExterno.agregarSecuencia(secLinkInscripcion);
        }

        if (evento.getInscripciones() != null) {
            Integer numeroInscritos = evento.getInscripciones().size();
            Secuencia secInscripciones = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NUMERO_INSCRITOS_EVENTO_EXTERNO), numeroInscritos.toString());
            secEventoExterno.agregarSecuencia(secInscripciones);
        }


        return secEventoExterno;
    }

    public Secuencia consultarcategoria(CategoriaEventoExterno categoria) {

        Secuencia secCategoria = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIA_EVENTO_EXTERNO), "");

        if (categoria.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CATEGORIA), categoria.getId().toString());
            secCategoria.agregarSecuencia(secId);
        }

        if (categoria.getNombre() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CATEGORIA), categoria.getNombre());
            secCategoria.agregarSecuencia(secNombre);
        }

        return secCategoria;
    }

    private Secuencia consultarCamposAdicionales(Collection<CampoAdicional> camposAdicionales) {

        Secuencia secCamposAdicionales = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CAMPOS_ADICIONALES), "");

        for (CampoAdicional campo : camposAdicionales) {
            Secuencia secCampoAdicional = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CAMPO_ADICIONAL), "");

            if (campo.getId() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), campo.getId().toString());
                secCampoAdicional.agregarSecuencia(secId);
            }

            if (campo.getValor() != null) {
                Secuencia secValorCampoAdicional = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR_CAMPO_ADICIONAL), campo.getValor());
                secCampoAdicional.agregarSecuencia(secValorCampoAdicional);
            }

            if (campo.getNombre() != null) {
                Secuencia secTipoCampo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), campo.getNombre());
                secCampoAdicional.agregarSecuencia(secTipoCampo);
            }

            secCamposAdicionales.agregarSecuencia(secCampoAdicional);
        }
        return secCamposAdicionales;
    }

    public Secuencia consultarPreguntas(Collection<Pregunta> preguntas) {
        Secuencia secPreguntas = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PREGUNTAS), "");
        for (Pregunta pregunta : preguntas) {
            Secuencia secPregunta = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PREGUNTA), "");

            if (pregunta.getId() != null) {
                Secuencia secIdPreg = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PREGUNTA), pregunta.getId().toString());
                secPregunta.agregarSecuencia(secIdPreg);
            }

            if (pregunta.getPregunta() != null) {
                Secuencia secPreg = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR_PREGUNTA), pregunta.getPregunta());
                secPregunta.agregarSecuencia(secPreg);
            }
            secPreguntas.agregarSecuencia(secPregunta);
        }
        return secPreguntas;
    }

    public EventoExterno consultarEventoExterno(Secuencia secEventoExterno) throws Exception {
        EventoExterno eventoExterno = new EventoExterno();

        Secuencia secId = secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_EVENTO_EXTERNO));
        if (secId != null) {
            eventoExterno.setId(Long.parseLong(secId.getValor()));
        }

        String titulo = secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO)) != null ? secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TITULO)).getValor() : null;
        eventoExterno.setTitulo(titulo);

        String descripcion = secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)) != null ? secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION)).getValor() : null;
        eventoExterno.setDescripcion(descripcion);

        Date fechaHora = formatoFechaHora.parse(secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_HORA)).getValor()) != null ? formatoFechaHora.parse(secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_HORA)).getValor()) : null;
        Timestamp ts = new Timestamp(fechaHora.getTime());
        eventoExterno.setFechaHoraInicio(ts);

        Date fechaLimiteInscripciones = formatoFechaHora.parse(secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_LIMITE_INSCRIPCIONES)).getValor()) != null ? formatoFechaHora.parse(secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_LIMITE_INSCRIPCIONES)).getValor()) : null;
        Timestamp tsf = new Timestamp(fechaLimiteInscripciones.getTime());
        eventoExterno.setFechaLimiteInscripciones(tsf);


        String rutaImagen = secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_IMAGEN)) != null ? secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_IMAGEN)).getValor() : null;
        eventoExterno.setRutaImagen(rutaImagen);

        Integer cupo = secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUPO)) != null ? Integer.parseInt(secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CUPO)).getValor()) : null;
        eventoExterno.setCupo(cupo);

        String estado = secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO)) != null ? secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO)).getValor() : null;
        eventoExterno.setEstado(estado);

        Secuencia secCategoria = secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIA_EVENTO_EXTERNO));
        if (secCategoria != null) {
            CategoriaEventoExterno categoria = consultarCategoria(secCategoria);
            eventoExterno.setCategoria(categoria);
        }


        Secuencia secPreguntas = secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PREGUNTAS));
        if (secPreguntas != null) {
            Collection<Pregunta> preguntas = consultarPreguntas(secPreguntas);
            eventoExterno.setPreguntas(preguntas);
        }

        Secuencia secCamposAdicionales = secEventoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CAMPOS_ADICIONALES));
        if (secCamposAdicionales != null) {
            Collection<CampoAdicional> camposAdicionales = consultarCamposAdicionales(secCamposAdicionales);
            eventoExterno.setCamposAdicionales(camposAdicionales);
        }
        return eventoExterno;
    }

    public Collection<CampoAdicional> consultarCamposAdicionales(Secuencia secCamposAdicionales) {
        Collection<CampoAdicional> camposAdicionales = new ArrayList<CampoAdicional>();
        Collection<Secuencia> secuenciaCamposAdicionales = secCamposAdicionales.getSecuencias();

        for (Secuencia sec : secuenciaCamposAdicionales) {
            CampoAdicional campoAdicional = new CampoAdicional();
            Secuencia secId = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CAMPO_ADICIONAL));
            if (secId != null) {
                campoAdicional.setId(Long.parseLong(secId.getValor()));
            }
            campoAdicional.setNombre(sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE)).getValor());
            campoAdicional.setValor(sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR_CAMPO_ADICIONAL)).getValor());
            camposAdicionales.add(campoAdicional);
        }

        return camposAdicionales;
    }

    public Collection<CategoriaEventoExterno> consultarCategoriasEventoExterno(Secuencia secCategorias) {
        Collection<CategoriaEventoExterno> categorias = new ArrayList<CategoriaEventoExterno>();
        Collection<Secuencia> secuenciaCategoriasEventoExterno = secCategorias.getSecuencias();
        for (Secuencia sec : secuenciaCategoriasEventoExterno) {
            CategoriaEventoExterno categoria = new CategoriaEventoExterno();

            Secuencia secId = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CATEGORIA));
            if (secId != null) {
                categoria.setId(Long.parseLong(secId.getValor()));
            }
            categoria.setNombre(sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CATEGORIA)).getValor());
            categorias.add(categoria);
        }
        return categorias;
    }

    public CategoriaEventoExterno consultarCategoria(Secuencia secCategoria) {

        CategoriaEventoExterno categoria = new CategoriaEventoExterno();

        Secuencia secId = secCategoria.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_CATEGORIA));
        if (secId != null) {
            categoria.setId(Long.parseLong(secId.getValor()));
        }
        Secuencia secNombre = secCategoria.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CATEGORIA));
        if (secNombre != null) {
            categoria.setNombre(secNombre.getValor());
        }

        //comprueba si la categoria existe y si no existe la crea
        CategoriaEventoExterno cat = categoriaFacade.find(Long.parseLong(secId.getValor()));
        if (cat == null) {
            categoriaFacade.create(categoria);
        }

        return categoria;
    }

    public Collection<Pregunta> consultarPreguntas(Secuencia secPreguntas) {
        Collection<Pregunta> preguntas = new ArrayList<Pregunta>();
        Collection<Secuencia> secuenciaPreguntas = secPreguntas.getSecuencias();
        for (Secuencia sec : secuenciaPreguntas) {
            Pregunta pregunta = new Pregunta();
            Secuencia secId = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_PREGUNTA));
            if (secId != null) {
                pregunta.setId(Long.parseLong(secId.getValor()));
            }
            Secuencia secValor = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR_PREGUNTA));
            if (secValor != null) {
                pregunta.setPregunta(secValor.getValor());
            }
            preguntas.add(pregunta);
        }
        return preguntas;
    }

    public Collection<TipoCampo> consultarTiposCampo(Secuencia secTiposCampo) {
        Collection<TipoCampo> tiposCampo = new ArrayList<TipoCampo>();
        Collection<Secuencia> secuenciaTiposCampo = secTiposCampo.getSecuencias();
        for (Secuencia sec : secuenciaTiposCampo) {
            TipoCampo tipoCampo = consultarTipoCampo(sec);

            tiposCampo.add(tipoCampo);
        }
        return tiposCampo;
    }

    public TipoCampo consultarTipoCampo(Secuencia secCamposAdicionales) {
        TipoCampo tipoCampo = new TipoCampo();
        Secuencia secId = secCamposAdicionales.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_TIPO_CAMPO));
        if (secId != null) {
            tipoCampo.setId(Long.parseLong(secId.getValor()));
        }
        tipoCampo.setNombre(secCamposAdicionales.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_TIPO_CAMPO)).getValor());
        tipoCampo.setObligatorio(Boolean.parseBoolean(secCamposAdicionales.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CAMPO_OBLIGATORIO)).getValor()));
        return tipoCampo;
    }

    public Secuencia consultarTiposCampo(Collection<TipoCampo> tipoCampo) {
        Secuencia secTiposCampo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPOS_CAMPO), "");
        for (TipoCampo tipo : tipoCampo) {
            if (tipo.getId() != null) {
                Secuencia secTipocampo = consultarTipoCampo(tipo);
                secTiposCampo.agregarSecuencia(secTipocampo);
            }
        }
        return secTiposCampo;
    }

    public Secuencia consultarTipoCampo(TipoCampo tipocampo) {
        Secuencia secTipoCampo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TIPO_CAMPO), "");

        Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tipocampo.getId().toString());
        secTipoCampo.agregarSecuencia(secId);

        Secuencia secNombreTipoCampo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_TIPO_CAMPO), tipocampo.getNombre());
        secTipoCampo.agregarSecuencia(secNombreTipoCampo);

        Secuencia secObligatorio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CAMPO_OBLIGATORIO), String.valueOf(tipocampo.isObligatorio()));
        secTipoCampo.agregarSecuencia(secObligatorio);

        return secTipoCampo;
    }
}
