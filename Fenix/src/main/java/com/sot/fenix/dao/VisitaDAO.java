package com.sot.fenix.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.sot.fenix.components.models.Visita;
import com.sot.fenix.components.models.facturacion.Facturacion.ESTADO;
import com.sot.fenix.templates.dao.ICentroIdDAO;

@Repository
public interface VisitaDAO extends ICentroIdDAO<Visita>{	
	public Visita findByCita_id(ObjectId citaId);
	
	/**
	 * Visitas de un centro cuyo numero de factura es mayor a #numeroFACTURA# y facturaGenerada=#facturaGenerada#
	 * devueltas en orden por facturaId
	 * @param centroId
	 * @param numeroFactura
	 * @return
	 */
	public List<Visita> findByCentro_idAndFacturacion_Factura_idFacturaGreaterThanEqualOrderByFacturacion_Factura_idFactura(ObjectId centroId, long numeroFactura);
	public List<Visita> findByCentro_idAndFacturacion_estadoAndFacturacion_FacturaIsNull(ObjectId centroId, ESTADO estado);
	
	public Visita findFirstByCentro_idAndFacturacion_Factura_idFacturaGreaterThanEqualOrderByFacturacion_Factura_idFacturaDesc(ObjectId centroId, long numeroFactura);
	
	
	public List<Visita> findByCentro_idOrderByFacturacion_Factura_idFacturaAsc(ObjectId centro);
}
