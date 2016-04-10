/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.uniandes.sisinfo.historico.serviciosnegocio.tesisM;

import co.uniandes.sisinfo.comun.constantes.Constantes;
import co.uniandes.sisinfo.historico.entities.tesisM.h_curso_tomado;
import co.uniandes.sisinfo.historico.entities.tesisM.h_detalleCalificacion_tesis_2;
import co.uniandes.sisinfo.historico.entities.tesisM.h_estudiante_maestria;
import co.uniandes.sisinfo.historico.entities.tesisM.h_inscripcion_subarea;
import co.uniandes.sisinfo.historico.entities.tesisM.h_jurado_tesis;
import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_1;
import co.uniandes.sisinfo.historico.entities.tesisM.h_tesis_2;
import co.uniandes.sisinfo.serviciosfuncionales.ServiceLocator;
import co.uniandes.sisinfo.serviciosfuncionales.parser.Secuencia;
import co.uniandes.sisinfo.serviciosnegocio.ConstanteRemote;
import java.io.File;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.NamingException;

/**
 *
 * @author Paola Andrea Gomez Barreto
 */
public class ConversorTesisMaestriaH {

    @EJB
    private ConstanteRemote constanteBean;
    private ServiceLocator serviceLocator;

    public ConversorTesisMaestriaH() {
        try {
            serviceLocator = new ServiceLocator();
            constanteBean = (ConstanteRemote) serviceLocator.getRemoteEJB(ConstanteRemote.class);
        } catch (NamingException ex) {
            Logger.getLogger(ConversorTesisMaestriaH.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Secuencia pasarEstudiantesHistoricoASecuenciaLigera(Collection<h_estudiante_maestria> estudiantesHistorico) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTES), "");

        for (h_estudiante_maestria estudianteHistorico : estudiantesHistorico) {
            Secuencia secTesis = pasarEstudianteHistoricoASecuenciaLigera(estudianteHistorico);
            secPrincipal.agregarSecuencia(secTesis);
        }
        return secPrincipal;
    }

    public Secuencia pasarEstudianteHistoricoASecuenciaLigera(h_estudiante_maestria estudianteHistorico) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");

        if (estudianteHistorico != null) {

            if (estudianteHistorico.getId() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), estudianteHistorico.getId().toString());
                secPrincipal.agregarSecuencia(secId);
            }

            if (estudianteHistorico.getCodigo() != null) {
                Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), estudianteHistorico.getCodigo());
                secPrincipal.agregarSecuencia(secCodigo);
            }

            if (estudianteHistorico.getCorreo() != null) {
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudianteHistorico.getCorreo());
                secPrincipal.agregarSecuencia(secCorreo);
            }

            if (estudianteHistorico.getNombres() != null) {
                Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudianteHistorico.getNombres());
                secPrincipal.agregarSecuencia(secNombres);
            }

            if (estudianteHistorico.getApellidos() != null) {
                Secuencia secApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudianteHistorico.getApellidos());
                secPrincipal.agregarSecuencia(secApellidos);
            }

            if (estudianteHistorico.getCorreoAsesor() != null) {
                Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");

                Secuencia secCorreoAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudianteHistorico.getCorreoAsesor());
                secAsesor.agregarSecuencia(secCorreoAsesor);

                if (estudianteHistorico.getNombresAsesor() != null) {
                    Secuencia secNombresAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudianteHistorico.getNombresAsesor());
                    secAsesor.agregarSecuencia(secNombresAsesor);
                }
                if (estudianteHistorico.getApellidosAsesor() != null) {
                    Secuencia secApellidosAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudianteHistorico.getApellidosAsesor());
                    secAsesor.agregarSecuencia(secApellidosAsesor);
                }
                secPrincipal.agregarSecuencia(secAsesor);
            }

            if (estudianteHistorico.getInfoT1Perdida() != null) {
                Secuencia secInfoT1Perdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_PERDIDA), estudianteHistorico.getInfoT1Perdida().toString());
                secPrincipal.agregarSecuencia(secInfoT1Perdida);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT1Perdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_PERDIDA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT1Perdida);
            }

            if (estudianteHistorico.getInfoT1Retirada() != null) {
                Secuencia secInfoT1Retirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_RETIRADA), estudianteHistorico.getInfoT1Retirada().toString());
                secPrincipal.agregarSecuencia(secInfoT1Retirada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT1Retirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_RETIRADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT1Retirada);
            }

            if (estudianteHistorico.getInfoT1Rechazada() != null) {
                Secuencia secInfoT1Rechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_RECHAZADA), estudianteHistorico.getInfoT1Rechazada().toString());
                secPrincipal.agregarSecuencia(secInfoT1Rechazada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT1Rechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_RECHAZADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT1Rechazada);
            }


            if (estudianteHistorico.getInfoT1Terminada() != null) {
                Secuencia secInfoT1Terminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_TERMINADA), estudianteHistorico.getInfoT1Terminada().toString());
                secPrincipal.agregarSecuencia(secInfoT1Terminada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT1Terminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_TERMINADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT1Terminada);
            }

            if (estudianteHistorico.getInfoT2Perdida() != null) {
                Secuencia secInfoT2Perdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_PERDIDA), estudianteHistorico.getInfoT2Perdida().toString());
                secPrincipal.agregarSecuencia(secInfoT2Perdida);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT2Perdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_PERDIDA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT2Perdida);
            }

            if (estudianteHistorico.getInfoT2Retirada() != null) {
                Secuencia secInfoT2Retirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_RETIRADA), estudianteHistorico.getInfoT2Retirada().toString());
                secPrincipal.agregarSecuencia(secInfoT2Retirada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT2Retirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_RETIRADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT2Retirada);
            }

            if (estudianteHistorico.getInfoT2Rechazada() != null) {
                Secuencia secInfoT2Rechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_RECHAZADA), estudianteHistorico.getInfoT2Rechazada().toString());
                secPrincipal.agregarSecuencia(secInfoT2Rechazada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT2Rechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_RECHAZADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT2Rechazada);
            }

            if (estudianteHistorico.getInfoT2Terminada() != null) {
                Secuencia secInfoT2Terminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_TERMINADA), estudianteHistorico.getInfoT2Terminada().toString());
                secPrincipal.agregarSecuencia(secInfoT2Terminada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT2Terminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_TERMINADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT2Terminada);
            }
        }
        return secPrincipal;
    }

    public Secuencia pasarEstudiantesHistoricoASecuenciaCompleta(Collection<h_estudiante_maestria> estudiantesHistorico) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTES), "");
        for (h_estudiante_maestria estudianteHistorico : estudiantesHistorico) {
            Secuencia secTesis = pasarEstudianteHistoricoASecuenciaCompleta(estudianteHistorico);
            secPrincipal.agregarSecuencia(secTesis);
        }
        return secPrincipal;
    }

    public Secuencia pasarEstudianteHistoricoASecuenciaCompleta(h_estudiante_maestria estudianteHistorico) {
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");

        if (estudianteHistorico != null) {

            if (estudianteHistorico.getId() != null) {
                Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), estudianteHistorico.getId().toString());
                secPrincipal.agregarSecuencia(secId);
            }

            if (estudianteHistorico.getCodigo() != null) {
                Secuencia secCodigo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE), estudianteHistorico.getCodigo());
                secPrincipal.agregarSecuencia(secCodigo);
            }

            if (estudianteHistorico.getCorreo() != null) {
                Secuencia secCorreo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudianteHistorico.getCorreo());
                secPrincipal.agregarSecuencia(secCorreo);
            }

            if (estudianteHistorico.getNombres() != null) {
                Secuencia secNombres = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudianteHistorico.getNombres());
                secPrincipal.agregarSecuencia(secNombres);
            }

            if (estudianteHistorico.getApellidos() != null) {
                Secuencia secApellidos = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudianteHistorico.getApellidos());
                secPrincipal.agregarSecuencia(secApellidos);
            }

            if (estudianteHistorico.getCorreoAsesor() != null) {
                Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");

                Secuencia secCorreoAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), estudianteHistorico.getCorreoAsesor());
                secAsesor.agregarSecuencia(secCorreoAsesor);

                if (estudianteHistorico.getNombresAsesor() != null) {
                    Secuencia secNombresAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), estudianteHistorico.getNombresAsesor());
                    secAsesor.agregarSecuencia(secNombresAsesor);
                }

                if (estudianteHistorico.getApellidosAsesor() != null) {
                    Secuencia secApellidosAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), estudianteHistorico.getApellidosAsesor());
                    secAsesor.agregarSecuencia(secApellidosAsesor);
                }

                secPrincipal.agregarSecuencia(secAsesor);
            }

            if (estudianteHistorico.getInscripcionSubarea() != null) {
                if (!estudianteHistorico.getInscripcionSubarea().isEmpty()) {
                    Secuencia secIncripcionesSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INSCRIPCIONES_SUBAREA), "");

                    Collection<h_inscripcion_subarea> inscripcionesSubarea = estudianteHistorico.getInscripcionSubarea();

                    for (h_inscripcion_subarea inscripcionSubarea : inscripcionesSubarea) {
                        Secuencia secIncripcionSubarea = pasarInscripcionSubAreaHistoricoASecuencia(inscripcionSubarea);
                        secIncripcionesSubarea.agregarSecuencia(secIncripcionSubarea);
                    }

                    secPrincipal.agregarSecuencia(secIncripcionesSubarea);
                }
            }


            if (estudianteHistorico.getTesis1() != null) {
                if (!estudianteHistorico.getTesis1().isEmpty()) {
                    Secuencia secTesises1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESISES1), "");
                    Collection<h_tesis_1> tesises1 = estudianteHistorico.getTesis1();
                    for (h_tesis_1 tesis1 : tesises1) {
                        Secuencia secTesis1 = pasarTesis1ASecuencia(tesis1);
                        secTesises1.agregarSecuencia(secTesis1);
                    }
                    secPrincipal.agregarSecuencia(secTesises1);
                }
            }


            if (estudianteHistorico.getTesis2() != null) {
                if (!estudianteHistorico.getTesis2().isEmpty()) {
                    Secuencia secTesises2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESISES2), "");
                    Collection<h_tesis_2> tesises2 = estudianteHistorico.getTesis2();
                    for (h_tesis_2 tesis2 : tesises2) {
                        Secuencia secTesis2 = pasarTesis2ASecuencia(tesis2);
                        secTesises2.agregarSecuencia(secTesis2);
                    }
                    secPrincipal.agregarSecuencia(secTesises2);
                }
            }

            if (estudianteHistorico.getInfoT1Perdida() != null) {
                Secuencia secInfoT1Perdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_PERDIDA), estudianteHistorico.getInfoT1Perdida().toString());
                secPrincipal.agregarSecuencia(secInfoT1Perdida);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT1Perdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_PERDIDA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT1Perdida);
            }

            if (estudianteHistorico.getInfoT1Retirada() != null) {
                Secuencia secInfoT1Retirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_RETIRADA), estudianteHistorico.getInfoT1Retirada().toString());
                secPrincipal.agregarSecuencia(secInfoT1Retirada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT1Retirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_RETIRADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT1Retirada);
            }

            if (estudianteHistorico.getInfoT1Rechazada() != null) {
                Secuencia secInfoT1Rechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_RECHAZADA), estudianteHistorico.getInfoT1Rechazada().toString());
                secPrincipal.agregarSecuencia(secInfoT1Rechazada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT1Rechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_RECHAZADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT1Rechazada);
            }


            if (estudianteHistorico.getInfoT1Terminada() != null) {
                Secuencia secInfoT1Terminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_TERMINADA), estudianteHistorico.getInfoT1Terminada().toString());
                secPrincipal.agregarSecuencia(secInfoT1Terminada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT1Terminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_1_TERMINADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT1Terminada);
            }

            if (estudianteHistorico.getInfoT2Perdida() != null) {
                Secuencia secInfoT2Perdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_PERDIDA), estudianteHistorico.getInfoT2Perdida().toString());
                secPrincipal.agregarSecuencia(secInfoT2Perdida);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT2Perdida = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_PERDIDA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT2Perdida);
            }

            if (estudianteHistorico.getInfoT2Retirada() != null) {
                Secuencia secInfoT2Retirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_RETIRADA), estudianteHistorico.getInfoT2Retirada().toString());
                secPrincipal.agregarSecuencia(secInfoT2Retirada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT2Retirada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_RETIRADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT2Retirada);
            }

            if (estudianteHistorico.getInfoT2Rechazada() != null) {
                Secuencia secInfoT2Rechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_RECHAZADA), estudianteHistorico.getInfoT2Rechazada().toString());
                secPrincipal.agregarSecuencia(secInfoT2Rechazada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT2Rechazada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_RECHAZADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT2Rechazada);
            }


            if (estudianteHistorico.getInfoT2Terminada() != null) {
                Secuencia secInfoT2Terminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_TERMINADA), estudianteHistorico.getInfoT2Terminada().toString());
                secPrincipal.agregarSecuencia(secInfoT2Terminada);
            } else {
                Boolean valorTemp = false;
                Secuencia secInfoT2Terminada = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS_2_TERMINADA), valorTemp.toString());
                secPrincipal.agregarSecuencia(secInfoT2Terminada);
            }
        }

        return secPrincipal;
    }

    public Secuencia pasarInscripcionSubAreaHistoricoASecuencia(h_inscripcion_subarea inscripcionSubarea) {
        Secuencia secIncripcionSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INSCRIPCION_SUBAREA), "");

        if (inscripcionSubarea.getNombreSubarea() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), inscripcionSubarea.getNombreSubarea());
            secIncripcionSubarea.agregarSecuencia(secNombre);
        } else {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), "");
            secIncripcionSubarea.agregarSecuencia(secNombre);
        }

        if (inscripcionSubarea.getCursos() != null || !inscripcionSubarea.getCursos().isEmpty()) {
            Secuencia secCursosSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSOS_SUBAREA), "");

            for (h_curso_tomado cursoTomado : inscripcionSubarea.getCursos()) {
                Secuencia secCursoSubarea = pasarCursoSubAreaHistoricoASecuencia(cursoTomado);
                secCursosSubarea.agregarSecuencia(secCursoSubarea);
            }

            secIncripcionSubarea.agregarSecuencia(secCursosSubarea);
        }

        return secIncripcionSubarea;
    }

    public Secuencia pasarCursoSubAreaHistoricoASecuencia(h_curso_tomado cursoTomado) {
        Secuencia secCursoSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_SUBAREA), "");

        if (cursoTomado.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), cursoTomado.getId().toString());
            secCursoSubarea.agregarSecuencia(secId);
        }

        if (cursoTomado.getNombre() != null) {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), cursoTomado.getNombre());
            secCursoSubarea.agregarSecuencia(secNombre);
        } else {
            Secuencia secNombre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), "");
            secCursoSubarea.agregarSecuencia(secNombre);
        }

        if (cursoTomado.getSemestre() != null) {
            Secuencia secSemestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), cursoTomado.getSemestre());
            secCursoSubarea.agregarSecuencia(secSemestre);
        } else {
            Secuencia secSemestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), "");
            secCursoSubarea.agregarSecuencia(secSemestre);
        }

        if (cursoTomado.getClasificacion() != null) {
            Secuencia secClasificacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CLASIFICACION), cursoTomado.getClasificacion());
            secCursoSubarea.agregarSecuencia(secClasificacion);
        } else {
            Secuencia secClasificacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CLASIFICACION), "");
            secCursoSubarea.agregarSecuencia(secClasificacion);
        }

        if (cursoTomado.getCursoVisto() != null) {
            Secuencia secCursoVisto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO), cursoTomado.getCursoVisto().toString());
            secCursoSubarea.agregarSecuencia(secCursoVisto);
        } else {
            Boolean valorTemp = false;
            Secuencia secCursoVisto = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CURSO_VISTO), valorTemp.toString());
            secCursoSubarea.agregarSecuencia(secCursoVisto);
        }

        return secCursoSubarea;
    }

    public Secuencia pasarTesis1ASecuencia(h_tesis_1 tesis1) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Secuencia secTesis1 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1), "");
        if (tesis1.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis1.getId().toString());
            secTesis1.agregarSecuencia(secId);
        }

        if (tesis1.getTema() != null) {
            Secuencia secTema = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), tesis1.getTema());
            secTesis1.agregarSecuencia(secTema);
        } else {
            Secuencia secTema = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), "");
            secTesis1.agregarSecuencia(secTema);
        }

        if (tesis1.getFechaCreacion() != null) {
            Secuencia secFechaCreacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), sdfHMS.format(new Date(tesis1.getFechaCreacion().getTime())));
            secTesis1.agregarSecuencia(secFechaCreacion);
        }

        if (tesis1.getFechaFin() != null) {
            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), sdfHMS.format(new Date(tesis1.getFechaCreacion().getTime())));
            secTesis1.agregarSecuencia(secFechaFin);
        }

        if (tesis1.getRutaArticulo() != null) {
            Secuencia secRutaArticulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_INICIO_TESIS1), tesis1.getRutaArticulo());
            secTesis1.agregarSecuencia(secRutaArticulo);
        }

        if (tesis1.getNota() != null) {
            Secuencia secNota = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), tesis1.getNota().toString());
            secTesis1.agregarSecuencia(secNota);
        }

        if (tesis1.getNotaAprobado() != null) {
            Secuencia secNotaAprobado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBADO_ASESOR), tesis1.getNotaAprobado());
            secTesis1.agregarSecuencia(secNotaAprobado);
        }

        if (tesis1.getCorreoAsesor() != null) {
            Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");

            Secuencia secCorreoAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), tesis1.getCorreoAsesor());
            secAsesor.agregarSecuencia(secCorreoAsesor);

            if (tesis1.getNombresAsesor() != null) {
                Secuencia secNombresAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), tesis1.getNombresAsesor());
                secAsesor.agregarSecuencia(secNombresAsesor);
            }

            if (tesis1.getApellidosAsesor() != null) {
                Secuencia secApellidosAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), tesis1.getApellidosAsesor());
                secAsesor.agregarSecuencia(secApellidosAsesor);
            }

            secTesis1.agregarSecuencia(secAsesor);
        }

        if (tesis1.getSemestre() != null) {
            Secuencia secSemestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), tesis1.getSemestre());
            secTesis1.agregarSecuencia(secSemestre);
        }

        if (tesis1.getEstado() != null) {
            Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS), tesis1.getEstado());
            secTesis1.agregarSecuencia(secEstado);
        }

        if (tesis1.getAprobadoParaParadigma() != null) {
            Secuencia secParadigma = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA), tesis1.getAprobadoParaParadigma().toString());
            secTesis1.agregarSecuencia(secParadigma);
        }
        return secTesis1;
    }

    public Secuencia pasarTesis2ASecuencia(h_tesis_2 tesis2) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Secuencia secTesis2 = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2), "");

        if (tesis2.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis2.getId().toString());
            secTesis2.agregarSecuencia(secId);
        }

        if (tesis2.getTemaTesis() != null) {
            Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), tesis2.getTemaTesis());
            secTesis2.agregarSecuencia(secTemaTesis);
        } else {
            Secuencia secTema = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), "");
            secTesis2.agregarSecuencia(secTema);
        }


        if (tesis2.getFechaCreacion() != null) {
            Secuencia secFechaCreacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), sdfHMS.format(new Date(tesis2.getFechaCreacion().getTime())));
            secTesis2.agregarSecuencia(secFechaCreacion);
        }

        if (tesis2.getFechaFin() != null) {
            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), sdfHMS.format(new Date(tesis2.getFechaCreacion().getTime())));
            secTesis2.agregarSecuencia(secFechaFin);
        }

        if (tesis2.getFechaSustentacion() != null) {
            Secuencia secFechaSustentacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SUSTENTACION), sdfHMS.format(new Date(tesis2.getFechaSustentacion().getTime())));
            secTesis2.agregarSecuencia(secFechaSustentacion);
        }

        if (tesis2.getRutaArticuloInicio() != null) {
            Secuencia secRutaArticuloInicio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE_INICIO_TESIS2), tesis2.getRutaArticuloInicio());
            secTesis2.agregarSecuencia(secRutaArticuloInicio);
        }

        if (tesis2.getRutaArticuloFin() != null) {
            Secuencia secRutaArticuloFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARTICULO_FIN_TESIS_2), tesis2.getRutaArticuloFin());
            secTesis2.agregarSecuencia(secRutaArticuloFin);
        }

        if (tesis2.getCalificacion() != null) {
            Secuencia secNota = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), tesis2.getCalificacion().toString());
            secTesis2.agregarSecuencia(secNota);
        }

        if (tesis2.getCorreoAsesor() != null) {
            Secuencia secAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");

            Secuencia secCorreoAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), tesis2.getCorreoAsesor());
            secAsesor.agregarSecuencia(secCorreoAsesor);

            if (tesis2.getNombresAsesor() != null) {
                Secuencia secNombresAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), tesis2.getNombresAsesor());
                secAsesor.agregarSecuencia(secNombresAsesor);
            }

            if (tesis2.getApellidosAsesor() != null) {
                Secuencia secApellidosAsesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), tesis2.getApellidosAsesor());
                secAsesor.agregarSecuencia(secApellidosAsesor);
            }

            secTesis2.agregarSecuencia(secAsesor);
        }

        if (tesis2.getSemestre() != null) {
            Secuencia secSemestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), tesis2.getSemestre());
            secTesis2.agregarSecuencia(secSemestre);
        }

        if (tesis2.getEstado() != null) {
            Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS), tesis2.getEstado());
            secTesis2.agregarSecuencia(secEstado);
        }

        if (tesis2.getJurados() != null) {
            Secuencia secJurados = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADOS_TESIS), "");
            Collection<h_jurado_tesis> jurados = tesis2.getJurados();
            for (h_jurado_tesis jurado : jurados) {
                Secuencia secJurado = pasarHistoricoJuradoASecuencia(jurado);
                secJurados.agregarSecuencia(secJurado);
            }
            secTesis2.agregarSecuencia(secJurados);
        }

        if (tesis2.getAprobadoParaParadigma() != null) {
            Secuencia secAprobadoParaParadigma = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA), tesis2.getAprobadoParaParadigma().toString());
            secTesis2.agregarSecuencia(secAprobadoParaParadigma);
        }
        return secTesis2;
    }

    //********** METODOS PARA HISTORICO TESIS 1 *************
    //Métodos auxiliares para paso de secuencias a entidades históricas
    public h_tesis_1 pasarSecuenciaATesis1Historica(Secuencia sec) {
        h_tesis_1 tesis = new h_tesis_1();
        Secuencia secId = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secId != null) {
            Long id = Long.parseLong(secId.getValor().trim());
            tesis.setId(id);
        }
        Secuencia secFechaCreacion = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION));
        if (secFechaCreacion != null) {
            String fechaCreacion = secFechaCreacion.getValor();
            try {
                SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date fecha = formatoDeFecha.parse(fechaCreacion);
                tesis.setFechaCreacion(new Timestamp(fecha.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(HistoricoTesis1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Secuencia secFechaFin = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
        if (secFechaFin != null) {
            String fechaFin = secFechaFin.getValor();
            try {
                SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date fecha = formatoDeFecha.parse(fechaFin);
                tesis.setFechaFin(new Timestamp(fecha.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(HistoricoTesis1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Secuencia secTemaTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS));
        if (secTemaTesis != null) {
            tesis.setTema(secTemaTesis.getValor());
        }
        Secuencia secRutaArticulo = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA));
        if (secRutaArticulo != null) {
            if (secRutaArticulo.getValor().contains(getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ARTICULO_TESIS1))) {
                tesis.setRutaArticulo(getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ARTICULO_TESIS1) + secRutaArticulo.getValor());
            } else {
                tesis.setRutaArticulo(secRutaArticulo.getValor());
            }
        }
        Secuencia secNota = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
        if (secNota != null) {
            tesis.setNotaAprobado(secNota.getValor());
        }
        Secuencia secEstudiante = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
        if (secEstudiante != null) {
            pasarSecuenciaAEstudiante(secEstudiante, tesis);
        }
        Secuencia secAsesor = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS));
        if (secAsesor != null) {
            pasarSecuenciaAProfesor(secAsesor, tesis);
        }
        Secuencia secSemestre = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
        if (secSemestre != null) {
            tesis.setSemestre(secSemestre.getValor());
        }
        Secuencia secEstadoTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS));
        if (secEstadoTesis != null) {
            tesis.setEstado(secEstadoTesis.getValor());
        }
        Secuencia secSubareaInvestigacion = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION));
        if (secSubareaInvestigacion != null) {
            Secuencia secNombreSubareaInvestigacion = secSubareaInvestigacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
            if (secNombreSubareaInvestigacion != null) {
                tesis.setSubareaInvestigacion(secNombreSubareaInvestigacion.getValor());
            }
        }
        return tesis;
    }

    public void pasarSecuenciaAEstudiante(Secuencia secEstudiante, h_tesis_1 tesis) {
        Secuencia secNombre = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            tesis.setNombresEstudiante(secNombre.getValor());
        }
        Secuencia secApp = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            tesis.setApellidosEstudiante(secApp.getValor());
        }
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            tesis.setCorreoEstudiante(secCorreo.getValor());
        }
    }

    public h_estudiante_maestria pasarSecuenciaAEstudiante(Secuencia secEstudiante) {
        h_estudiante_maestria estudianteMaestria = new h_estudiante_maestria();

        Secuencia secNombre = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            estudianteMaestria.setNombres(secNombre.getValor());
        }
        Secuencia secApp = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            estudianteMaestria.setApellidos(secApp.getValor());
        }
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            estudianteMaestria.setCorreo(secCorreo.getValor());
        }
        Secuencia secCodigo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE));
        if (secCodigo != null) {
            estudianteMaestria.setCodigo(secCodigo.getValor());
        }

        return estudianteMaestria;
    }

    public void pasarSecuenciaAProfesor(Secuencia secuencia, h_tesis_1 p) {
        Secuencia secProfesor = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));
        Secuencia secNombre = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            p.setNombresAsesor(secNombre.getValor());
        }
        Secuencia secApp = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            p.setApellidosAsesor(secApp.getValor());
        }
        Secuencia secCorreo = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            p.setCorreoAsesor(secCorreo.getValor());
        }
    }

    public String pasarSecuenciaAPeriodo(Secuencia secSemestre) {
        Secuencia secPeriodo = secSemestre.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (secPeriodo != null) {
            return secPeriodo.getValor();
        }
        return null;
    }

    //Métodos auxiliares para paso de entidades históricas a secuencias
    public Secuencia pasarHistoricoTesises1ASecuencia(List<h_tesis_1> tesises) {
        Secuencia secTesises = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), "");
        for (h_tesis_1 tesis : tesises) {
            Secuencia secTesis = pasarHistoricoTesis1ASecuencia(tesis);
            secTesises.agregarSecuencia(secTesis);
        }
        return secTesises;
    }

    public Secuencia pasarHistoricoTesis1ASecuencia(h_tesis_1 tesis) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS1), "");
        if (tesis.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }
        if (tesis.getFechaCreacion() != null) {
            Secuencia secFechaCreacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), sdfHMS.format(new Date(tesis.getFechaCreacion().getTime())));
            secPrincipal.agregarSecuencia(secFechaCreacion);
        }
        if (tesis.getFechaFin() != null) {
            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), sdfHMS.format(new Date(tesis.getFechaFin().getTime())));
            secPrincipal.agregarSecuencia(secFechaFin);
        }
        if (tesis.getTema() != null) {
            Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), tesis.getTema());
            secPrincipal.agregarSecuencia(secTemaTesis);
        }
        if (tesis.getRutaArticulo() != null) {
            Secuencia secRutaArticulo = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA), tesis.getRutaArticulo());
            secPrincipal.agregarSecuencia(secRutaArticulo);
        }
        if (tesis.getNota() != null) {
            Secuencia secNota = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), tesis.getNota().toString());
            secPrincipal.agregarSecuencia(secNota);
        }
        Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
        if (tesis.getNombresEstudiante() != null) {
            Secuencia secNombresEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), tesis.getNombresEstudiante());
            secEstudiante.agregarSecuencia(secNombresEstudiante);
        }
        if (tesis.getApellidosEstudiante() != null) {
            Secuencia secApellidosEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), tesis.getApellidosEstudiante());
            secEstudiante.agregarSecuencia(secApellidosEstudiante);
        }
        if (tesis.getCorreoEstudiante() != null) {
            Secuencia secCorreoEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), tesis.getCorreoEstudiante());
            secEstudiante.agregarSecuencia(secCorreoEstudiante);
        }
        secPrincipal.agregarSecuencia(secEstudiante);
        Secuencia secAsesorTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
        Secuencia secProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");
        if (tesis.getNombresEstudiante() != null) {
            Secuencia secNombresProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), tesis.getNombresAsesor());
            secProfesor.agregarSecuencia(secNombresProfesor);
        }
        if (tesis.getApellidosEstudiante() != null) {
            Secuencia secApellidosProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), tesis.getApellidosAsesor());
            secProfesor.agregarSecuencia(secApellidosProfesor);
        }
        if (tesis.getCorreoEstudiante() != null) {
            Secuencia secCorreoProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), tesis.getCorreoAsesor());
            secProfesor.agregarSecuencia(secCorreoProfesor);
        }
        secAsesorTesis.agregarSecuencia(secProfesor);
        secPrincipal.agregarSecuencia(secAsesorTesis);
        if (tesis.getSemestre() != null) {
            Secuencia secSemestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), tesis.getSemestre());
            secPrincipal.agregarSecuencia(secSemestre);
        }
        if (tesis.getEstado() != null) {
            Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS), tesis.getEstado());
            secPrincipal.agregarSecuencia(secEstado);
        }
        if (tesis.getSubareaInvestigacion() != null) {
            Secuencia secSubareaInvestigacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION), "");
            Secuencia secNombreSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), tesis.getSubareaInvestigacion());
            secSubareaInvestigacion.agregarSecuencia(secNombreSubarea);
            secPrincipal.agregarSecuencia(secSubareaInvestigacion);
        }
        return secPrincipal;
    }

    //********** METODOS PARA HISTORICO TESIS 2 *************
    //Métodos auxiliares para paso de secuencias a entidades históricas
    public h_tesis_2 pasarSecuenciaATesis2Historica(Secuencia sec) {

        h_tesis_2 hTesis2 = new h_tesis_2();
        Secuencia secId = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL));
        if (secId != null) {
            Long id = Long.parseLong(secId.getValor().trim());
            hTesis2.setId(id);
        }

        Secuencia secFechaCreacion = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION));
        if (secFechaCreacion != null) {
            String fechaCreacion = secFechaCreacion.getValor();
            try {
                SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date fecha = formatoDeFecha.parse(fechaCreacion);
                hTesis2.setFechaCreacion(new Timestamp(fecha.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(HistoricoTesis2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Secuencia secFechaFin = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN));
        if (secFechaFin != null) {
            String fechaFin = secFechaFin.getValor();
            try {
                SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date fecha = formatoDeFecha.parse(fechaFin);
                hTesis2.setFechaFin(new Timestamp(fecha.getTime()));
            } catch (ParseException ex) {
                Logger.getLogger(HistoricoTesis2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Secuencia secHorarioSustentacion = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_HORARIO_SUSTENTACION));
        if (secHorarioSustentacion != null) {
            Secuencia secFechaSustentacion = secHorarioSustentacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SUSTENTACION));
            if (secFechaSustentacion != null) {
                String fechaSustentacion = secFechaSustentacion.getValor();
                try {
                    SimpleDateFormat formatoDeFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date fecha = formatoDeFecha.parse(fechaSustentacion);
                    hTesis2.setFechaSustentacion(new Timestamp(fecha.getTime()));
                } catch (ParseException ex) {
                    Logger.getLogger(HistoricoTesis2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        Secuencia secTemaTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS));
        if (secTemaTesis != null) {
            hTesis2.setTemaTesis(secTemaTesis.getValor());
        }
        Secuencia secNota = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
        if (secNota != null) {
            Double nota = Double.parseDouble(secNota.getValor());
            hTesis2.setCalificacion(nota);
        }
        Secuencia secNotaSustentacion = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_SUSTENTACION_TESIS2));
        if (secNotaSustentacion != null) {
            Double nota = Double.parseDouble(secNotaSustentacion.getValor());
            hTesis2.setNotaSustentacion(nota);
        }
        Secuencia secRutaArticuloInicio = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA));
        if (secRutaArticuloInicio != null) {
            if (!secRutaArticuloInicio.getValor().contains(getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ARTICULO_TESIS2))) {
                hTesis2.setRutaArticuloInicio(getConstanteBean().getConstante(Constantes.RUTA_ARCHIVO_ARTICULO_TESIS2) + secRutaArticuloInicio.getValor());
            } else {
                hTesis2.setRutaArticuloInicio(secRutaArticuloInicio.getValor());
            }
        }
        Secuencia secRutaArticuloFin = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARTICULO_FIN_TESIS_2));
        if (secRutaArticuloFin != null) {
            hTesis2.setRutaArticuloFin(secRutaArticuloFin.getValor());
        }
        Secuencia secEstudiante = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE));
        if (secEstudiante != null) {
            pasarSecuenciaAEstudiante(secEstudiante, hTesis2);
        }
        Secuencia secAsesor = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS));
        if (secAsesor != null) {
            pasarSecuenciaAProfesor(secAsesor, hTesis2);
            /*System.out.println("\n\n ahora tesis tiene:" + tesis.getNombresAsesor() + "\n\n");
            System.out.println("\n\n ahora tesis tiene:" + tesis.getApellidosAsesor() + "\n\n");
            System.out.println("\n\n ahora tesis tiene:" + tesis.getCorreoAsesor() + "\n\n");*/
        }
        Secuencia secCalificacionJurados = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACIONES_JURADOS));
        if (secCalificacionJurados != null) {
            pasarSecuenciaAJurados(secCalificacionJurados, hTesis2);
        }
        Secuencia secSemestre = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE));
        if (secSemestre != null) {
            hTesis2.setSemestre(secSemestre.getValor());
        }

        Secuencia secEstadoTesis = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS));
        if (secEstadoTesis != null) {
            hTesis2.setEstado(secEstadoTesis.getValor());
        }

        Secuencia secAprobadoParaParadigma = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APROBACION_ARTICULO_PARADIGMA));
        if (secAprobadoParaParadigma != null) {
            hTesis2.setAprobadoParaParadigma(Boolean.parseBoolean(secAprobadoParaParadigma.getValor()));
        }

        Secuencia secSubareaInvestigacion = sec.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION));
        if (secSubareaInvestigacion != null) {
            Secuencia secNombreSubareaInvestigacion = secSubareaInvestigacion.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
            if (secNombreSubareaInvestigacion != null) {
                hTesis2.setSubareaInvestigacion(secNombreSubareaInvestigacion.getValor());
            }
        }
        return hTesis2;
    }

    public void pasarSecuenciaAEstudiante(Secuencia secEstudiante, h_tesis_2 tesis) {
        Secuencia secNombre = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            tesis.setNombresEstudiante(secNombre.getValor());
        }
        Secuencia secApp = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            tesis.setApellidosEstudiante(secApp.getValor());
        }
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            tesis.setCorreoEstudiante(secCorreo.getValor());
        }
    }

    public h_estudiante_maestria pasarSecuenciaAEstudianteConAsesor(Secuencia secEstudiante, h_tesis_2 tesis) {
        h_estudiante_maestria estudianteMaestria = new h_estudiante_maestria();

        Secuencia secNombre = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            estudianteMaestria.setNombres(secNombre.getValor());
        }
        Secuencia secApp = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            estudianteMaestria.setApellidos(secApp.getValor());
        }
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            estudianteMaestria.setCorreo(secCorreo.getValor());
        }
        Secuencia secCodigo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE));
        if (secCodigo != null) {
            estudianteMaestria.setCodigo(secCodigo.getValor());
        }

        if (tesis.getNombresAsesor() != null) {
            System.out.println("\n\nEn pasarSecuenciaAEstudianteConAsesor nombreAsesor no es null \n\n");
            estudianteMaestria.setNombresAsesor(tesis.getNombresAsesor());
        }

        if (tesis.getApellidosAsesor() != null) {
            estudianteMaestria.setApellidosAsesor(tesis.getApellidosAsesor());
        }

        if (tesis.getCorreoAsesor() != null) {
            estudianteMaestria.setCorreoAsesor(tesis.getCorreoAsesor());
        }


        return estudianteMaestria;
    }

    public h_estudiante_maestria pasarSecuenciaAEstudianteSimple(Secuencia secEstudiante) {
        h_estudiante_maestria estudianteMaestria = new h_estudiante_maestria();

        Secuencia secNombre = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            estudianteMaestria.setNombres(secNombre.getValor());
        }
        Secuencia secApp = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            estudianteMaestria.setApellidos(secApp.getValor());
        }
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            estudianteMaestria.setCorreo(secCorreo.getValor());
        }
        Secuencia secCodigo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CODIGO_ESTUDIANTE));
        if (secCodigo != null) {
            estudianteMaestria.setCodigo(secCodigo.getValor());
        }

        return estudianteMaestria;
    }

    public String pasarSecuenciaAEstudianteCorreo(Secuencia secEstudiante) {
        Secuencia secCorreo = secEstudiante.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            return secCorreo.getValor();
        }

        return null;
    }

    public void pasarSecuenciaAProfesor(Secuencia secuencia, h_tesis_2 p) {

        System.out.println("\n\nENTRO A pasarSecuenciaAProfesor\n\n");

        Secuencia secProfesor = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR));

        Secuencia secNombre = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            System.out.println("\n\n secNombre no es null\n\n");
            p.setNombresAsesor(secNombre.getValor());
        }
        Secuencia secApp = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            System.out.println("\n\n secApp no es null\n\n");
            p.setApellidosAsesor(secApp.getValor());
        }
        Secuencia secCorreo = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            System.out.println("\n\n secCorreo no es null\n\n");
            p.setCorreoAsesor(secCorreo.getValor());
        }
    }

    public void pasarSecuenciaAProfesorJurado(Secuencia secuencia, h_jurado_tesis jurado) {
        Secuencia secJuradoExterno = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADO_TESIS_EXTERNO));
        if (secJuradoExterno != null) {
            Secuencia secNombre = secJuradoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
            if (secNombre != null) {
                jurado.setNombres(secNombre.getValor());
            }
            Secuencia secApp = secJuradoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
            if (secApp != null) {
                jurado.setApellidos(secApp.getValor());
            }
            Secuencia secCorreo = secJuradoExterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo != null) {
                jurado.setCorreo(secCorreo.getValor());
            }
            Secuencia secDetalleCalificacionJurado = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIAS_CRITERIOS_TESIS_2));
            if (secDetalleCalificacionJurado != null) {
                pasarSecuenciaADetallesCalificacion(secDetalleCalificacionJurado, jurado);
            }

        }
        Secuencia secJuradoInterno = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADO_TESIS_INTERNO));
        if (secJuradoInterno != null) {
            Secuencia secNombre = secJuradoInterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
            if (secNombre != null) {
                jurado.setNombres(secNombre.getValor());
            }
            Secuencia secApp = secJuradoInterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
            if (secApp != null) {
                jurado.setApellidos(secApp.getValor());
            }
            Secuencia secCorreo = secJuradoInterno.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo != null) {
                jurado.setCorreo(secCorreo.getValor());
            }
            Secuencia secDetalleCalificacionJurado = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIAS_CRITERIOS_TESIS_2));
            if (secDetalleCalificacionJurado != null) {
                pasarSecuenciaADetallesCalificacion(secDetalleCalificacionJurado, jurado);
            }
        }

        Secuencia secCoasesor = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COASESOR));
        if (secCoasesor != null) {
            Secuencia secNombre = secCoasesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
            if (secNombre != null) {
                jurado.setNombres(secNombre.getValor());
            }
            Secuencia secApp = secCoasesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
            if (secApp != null) {
                jurado.setApellidos(secApp.getValor());
            }
            Secuencia secCorreo = secCoasesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
            if (secCorreo != null) {
                jurado.setCorreo(secCorreo.getValor());
            }
            Secuencia secDetalleCalificacionJurado = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CATEGORIAS_CRITERIOS_TESIS_2));
            if (secDetalleCalificacionJurado != null) {
                pasarSecuenciaADetallesCalificacion(secDetalleCalificacionJurado, jurado);
            }

        }

        Secuencia secNota = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
        if (secNota != null) {
            Double nota = Double.parseDouble(secNota.getValor());
            jurado.setNota(nota);
        }
    }

    public void pasarSecuenciaAJuradoExterno(Secuencia secuencia, h_jurado_tesis jurado, boolean esExterno) {
        Secuencia secProfesor = secuencia.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADO_TESIS_EXTERNO));
        Secuencia secNombre = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES));
        if (secNombre != null) {
            jurado.setNombres(secNombre.getValor());
        }
        Secuencia secApp = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS));
        if (secApp != null) {
            jurado.setApellidos(secApp.getValor());
        }
        Secuencia secCorreo = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO));
        if (secCorreo != null) {
            jurado.setCorreo(secCorreo.getValor());
        }
        Secuencia secNota = secProfesor.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA));
        if (secNota != null) {
            Double nota = Double.parseDouble(secNota.getValor());
            jurado.setNota(nota);
        }
        jurado.setExterno(esExterno);
    }

    public void pasarSecuenciaAJurados(Secuencia secCalificacionesJurado, h_tesis_2 tesis) {
        Secuencia secCalificacionJurado = secCalificacionesJurado.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACION_JURADO));
        Collection<h_jurado_tesis> jurados = new ArrayList<h_jurado_tesis>();
        for (Secuencia secCalificacion : secCalificacionesJurado.getSecuencias()) {
            h_jurado_tesis jurado = new h_jurado_tesis();
            pasarSecuenciaAProfesorJurado(secCalificacion, jurado);
            jurados.add(jurado);
        }
        tesis.setJurados(jurados);
    }

    public void pasarSecuenciaADetallesCalificacion(Secuencia secDetallesCalificacionJurado, h_jurado_tesis jurado) {

        Collection<h_detalleCalificacion_tesis_2> calificacionesCriterios = new ArrayList<h_detalleCalificacion_tesis_2>();
        for (Secuencia secDetallesCategoria : secDetallesCalificacionJurado.getSecuencias()) {

            Secuencia secCalificacionCriterios = secDetallesCategoria.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACION_CRITERIOS));
            for (Secuencia secCalificacionCriterio : secCalificacionCriterios.getSecuencias()) {

                h_detalleCalificacion_tesis_2 calificacionCriterio = new h_detalleCalificacion_tesis_2();

                Secuencia secCategoriaNombre = secDetallesCategoria.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE_CATEGORIA));
                if (secCategoriaNombre != null) {
                    calificacionCriterio.setCategoriaNombre(secCategoriaNombre.getValor());
                }

                Secuencia secCategoriaDescripcion = secDetallesCategoria.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION));
                if (secCategoriaDescripcion != null) {
                    calificacionCriterio.setCategoriaDescripcion(secCategoriaDescripcion.getValor());
                }

                Secuencia secCategoriaPorcentaje = secDetallesCategoria.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PORCENTAJE_CATEGORIA));
                if (secCategoriaPorcentaje != null) {
                    Double porcentaje = Double.parseDouble(secCategoriaPorcentaje.getValor());
                    calificacionCriterio.setCategoriaPorcentaje(porcentaje);
                }

                Secuencia secCalificacionCriterio1 = secCalificacionCriterio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CALIFICACION_CRITERIO));
                pasarSecuenciaADetallesCalificacionCriterioTesis2(secCalificacionCriterio1, calificacionCriterio);

                calificacionesCriterios.add(calificacionCriterio);
            }

        }
        jurado.setDetallesCalificacion(calificacionesCriterios);
    }

    public void pasarSecuenciaADetallesCalificacionCriterioTesis2(Secuencia secCalificacionCriterio, h_detalleCalificacion_tesis_2 calificacionCriterio) {

        Secuencia secCriterioNombre = secCalificacionCriterio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE));
        if (secCriterioNombre != null) {
            calificacionCriterio.setCriterioNombre(secCriterioNombre.getValor());
        }

        Secuencia secCriterioDescripcion = secCalificacionCriterio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_DESCRIPCION));
        if (secCriterioDescripcion != null) {
            calificacionCriterio.setCriterioDescripcion(secCriterioDescripcion.getValor());
        }

        Secuencia secCriterioPorcentaje = secCalificacionCriterio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PESO));
        if (secCriterioPorcentaje != null) {
            Double porcentaje = Double.parseDouble(secCriterioPorcentaje.getValor());
            calificacionCriterio.setCriterioPeso(porcentaje);
        }

        Secuencia secTextoComentario = secCalificacionCriterio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_COMENTARIO));
        if (secTextoComentario != null) {
            calificacionCriterio.setCriterioComentario(secTextoComentario.getValor());
        }

        Secuencia secValor = secCalificacionCriterio.obtenerPrimeraSecuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_VALOR));
        if (secValor != null) {
            Double valor = Double.parseDouble(secValor.getValor());
            calificacionCriterio.setCriterioValor(valor);
        }

    }

    //Métodos auxiliares para paso de entidades históricas a secuencias
    public Secuencia pasarHistoricoTesises2ASecuencia(List<h_tesis_2> tesises) {
        Secuencia secTesises = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_INFORMACION_TESISES), "");
        for (h_tesis_2 tesis : tesises) {
            Secuencia secTesis = pasarHistoricoTesis2ASecuencia(tesis);
            secTesises.agregarSecuencia(secTesis);
        }
        return secTesises;
    }

    public Secuencia pasarHistoricoTesis2ASecuencia(h_tesis_2 tesis) {
        SimpleDateFormat sdfHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Secuencia secPrincipal = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TESIS2), "");

        if (tesis.getId() != null) {
            Secuencia secId = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ID_GENERAL), tesis.getId().toString());
            secPrincipal.agregarSecuencia(secId);
        }

        if (tesis.getFechaCreacion() != null) {
            Secuencia secFechaCreacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_CREACION), sdfHMS.format(new Date(tesis.getFechaCreacion().getTime())));
            secPrincipal.agregarSecuencia(secFechaCreacion);
        }

        if (tesis.getFechaFin() != null) {
            Secuencia secFechaFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_FIN), sdfHMS.format(new Date(tesis.getFechaFin().getTime())));
            secPrincipal.agregarSecuencia(secFechaFin);
        }

        if (tesis.getFechaSustentacion() != null) {
            Secuencia secFechaSustentacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_FECHA_SUSTENTACION), sdfHMS.format(new Date(tesis.getFechaSustentacion().getTime())));
            secPrincipal.agregarSecuencia(secFechaSustentacion);
        }

        if (tesis.getTemaTesis() != null) {
            Secuencia secTemaTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_TEMA_TESIS), tesis.getTemaTesis());
            secPrincipal.agregarSecuencia(secTemaTesis);
        }

        if (tesis.getRutaArticuloInicio() != null) {
            Secuencia secRutaArticuloInicio = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA), tesis.getRutaArticuloInicio());
            secPrincipal.agregarSecuencia(secRutaArticuloInicio);
        }

        if (tesis.getRutaArticuloFin() != null) {
            Secuencia secRutaArticuloFin = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_RUTA_ARTICULO_FIN_TESIS_2), tesis.getRutaArticuloFin());
            secPrincipal.agregarSecuencia(secRutaArticuloFin);
        }

        if (tesis.getCalificacion() != null) {
            Secuencia secNota = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), tesis.getCalificacion().toString());
            secPrincipal.agregarSecuencia(secNota);
        }

        if (tesis.getNotaSustentacion() != null) {
            Secuencia secNota = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA_SUSTENTACION_TESIS2), tesis.getNotaSustentacion().toString());
            secPrincipal.agregarSecuencia(secNota);
        }

        Secuencia secEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTUDIANTE), "");
        if (tesis.getNombresEstudiante() != null) {
            Secuencia secNombresEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), tesis.getNombresEstudiante());
            secEstudiante.agregarSecuencia(secNombresEstudiante);
        }

        if (tesis.getApellidosEstudiante() != null) {
            Secuencia secApellidosEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), tesis.getApellidosEstudiante());
            secEstudiante.agregarSecuencia(secApellidosEstudiante);
        }

        if (tesis.getCorreoEstudiante() != null) {
            Secuencia secCorreoEstudiante = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), tesis.getCorreoEstudiante());
            secEstudiante.agregarSecuencia(secCorreoEstudiante);
        }
        secPrincipal.agregarSecuencia(secEstudiante);
        Secuencia secAsesorTesis = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ASESOR_TESIS), "");
        Secuencia secProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");

        if (tesis.getNombresEstudiante() != null) {
            Secuencia secNombresProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), tesis.getNombresAsesor());
            secProfesor.agregarSecuencia(secNombresProfesor);
        }

        if (tesis.getApellidosEstudiante() != null) {
            Secuencia secApellidosProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), tesis.getApellidosAsesor());
            secProfesor.agregarSecuencia(secApellidosProfesor);
        }

        if (tesis.getCorreoEstudiante() != null) {
            Secuencia secCorreoProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_CORREO), tesis.getCorreoAsesor());
            secProfesor.agregarSecuencia(secCorreoProfesor);
        }
        secAsesorTesis.agregarSecuencia(secProfesor);
        secPrincipal.agregarSecuencia(secAsesorTesis);

        if (tesis.getSemestre() != null) {
            Secuencia secSemestre = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_SEMESTRE), tesis.getSemestre());
            secPrincipal.agregarSecuencia(secSemestre);
        }

        if (tesis.getEstado() != null) {
            Secuencia secEstado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_ESTADO_TESIS), tesis.getEstado());
            secPrincipal.agregarSecuencia(secEstado);
        }

        if (tesis.getSubareaInvestigacion() != null) {
            Secuencia secSubareaInvestigacion = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_GRUPO_INVESTIGACION), "");
            Secuencia secNombreSubarea = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRE), tesis.getSubareaInvestigacion());
            secSubareaInvestigacion.agregarSecuencia(secNombreSubarea);
            secPrincipal.agregarSecuencia(secSubareaInvestigacion);
        }

        if (tesis.getJurados() != null) {
            Secuencia secJurados = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADOS_TESIS), "");
            Collection<h_jurado_tesis> jurados = tesis.getJurados();
            for (h_jurado_tesis jurado : jurados) {
                Secuencia secJurado = pasarHistoricoJuradoASecuencia(jurado);
                secJurados.agregarSecuencia(secJurado);
            }
            secPrincipal.agregarSecuencia(secJurados);
        }

        return secPrincipal;
    }

    public Secuencia pasarHistoricoJuradoASecuencia(h_jurado_tesis jurado) {
        Secuencia secJurado = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_JURADO_TESIS), "");

        Secuencia secProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_PROFESOR), "");
        if (jurado.getNombres() != null) {
            Secuencia secNombresProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOMBRES), jurado.getNombres());
            secProfesor.agregarSecuencia(secNombresProfesor);
        }
        if (jurado.getApellidos() != null) {
            Secuencia secApellidosProfesor = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_APELLIDOS), jurado.getApellidos());
            secProfesor.agregarSecuencia(secApellidosProfesor);
        }
        secJurado.agregarSecuencia(secProfesor);

        if (jurado.getNota() != null) {
            Secuencia secNota = new Secuencia(getConstanteBean().getConstante(Constantes.TAG_PARAM_NOTA), jurado.getNota().toString());
            secJurado.agregarSecuencia(secNota);
        }

        return secJurado;
    }

    //********** METODOS AUXILIARES DE RETORNO DE ATRIBUTOS *************
    public ConstanteRemote getConstanteBean() {
        return constanteBean;
    }
}
