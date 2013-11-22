//$HeadURL$
/*----------------------------------------------------------------------------
 This file is part of deegree, http://deegree.org/
 Copyright (C) 2001-2010 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -

 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
 You should have received a copy of the GNU Lesser General Public License
 along with this library; if not, write to the Free Software Foundation, Inc.,
 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

 Contact information:

 lat/lon GmbH
 Aennchenstr. 19, 53177 Bonn
 Germany
 http://lat-lon.de/

 Department of Geography, University of Bonn
 Prof. Dr. Klaus Greve
 Postfach 1147, 53001 Bonn
 Germany
 http://www.geographie.uni-bonn.de/deegree/

 Occam Labs Schmitz & Schneider GbR
 Godesberger Allee 139, 53175 Bonn
 Germany
 http://www.occamlabs.de/

 e-mail: info@deegree.org
 ----------------------------------------------------------------------------*/
package de.hwbllmnn.deegree.layer.mapnik;

import java.io.File;
import java.util.List;

import mapnik.Box2d;

import org.deegree.commons.ows.exception.OWSException;
import org.deegree.geometry.Envelope;
import org.deegree.layer.AbstractLayer;
import org.deegree.layer.LayerQuery;
import org.deegree.layer.metadata.LayerMetadata;

/**
 * 
 * @author <a href="mailto:schmitz@lat-lon.de">Andreas Schmitz</a>
 * @author last edited by: $Author: stranger $
 * 
 * @version $Revision: $, $Date: $
 */
public class MapnikLayer extends AbstractLayer {

    private File config;

    public MapnikLayer( LayerMetadata md, File config ) {
        super( md );
        this.config = config;
    }

    @Override
    public MapnikLayerData mapQuery( final LayerQuery query, List<String> headers )
                            throws OWSException {
        final Envelope bbox = query.getQueryBox();
        Box2d box = new Box2d( bbox.getMin().get0(), bbox.getMin().get1(), bbox.getMax().get0(), bbox.getMax().get1() );
        return new MapnikLayerData( box, query.getWidth(), query.getHeight(), config, bbox.getCoordinateSystem().getAlias() );
    }

    @Override
    public MapnikLayerData infoQuery( final LayerQuery query, List<String> headers )
                            throws OWSException {
        return null;
    }

}
