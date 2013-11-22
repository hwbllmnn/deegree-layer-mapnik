/*----------------------------------------------------------------------------
 This file is part of deegree
 Copyright (C) 2001-2013 by:
 - Department of Geography, University of Bonn -
 and
 - lat/lon GmbH -
 and
 - Occam Labs UG (haftungsbeschränkt) -
 and others

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

 e-mail: info@deegree.org
 website: http://www.deegree.org/
----------------------------------------------------------------------------*/
package de.hwbllmnn.deegree.layer.mapnik;

import static org.slf4j.LoggerFactory.getLogger;

import java.net.URL;

import mapnik.Mapnik;

import org.deegree.layer.persistence.LayerStore;
import org.deegree.layer.persistence.LayerStoreProvider;
import org.deegree.workspace.ResourceLocation;
import org.deegree.workspace.ResourceMetadata;
import org.deegree.workspace.Workspace;
import org.slf4j.Logger;

/**
 * SPI provider implementation for feature layer stores.
 * 
 * @author <a href="mailto:schmitz@occamlabs.de">Andreas Schmitz</a>
 * 
 * @since 3.4
 */
public class MapnikLayerStoreProvider extends LayerStoreProvider {

    private static final Logger LOG = getLogger( MapnikLayerStoreProvider.class );

    private static final URL SCHEMA_URL = MapnikLayerStoreProvider.class.getResource( "/META-INF/schemas/layers/mapnik/3.4.0/mapnik.xsd" );

    public MapnikLayerStoreProvider() {
        try {
            Mapnik.initialize();
        } catch ( UnsatisfiedLinkError e ) {
            LOG.info( "Could not initialize mapnik jni binding." );
            LOG.info( "If you just reloaded the workspace this is fine; multiple jni bindings loading is not supported by Java." );
        }
    }

    @Override
    public String getNamespace() {
        return "http://hwbllmnn.de/deegree/layers/mapnik";
    }

    @Override
    public ResourceMetadata<LayerStore> createFromLocation( Workspace workspace, ResourceLocation<LayerStore> location ) {
        return new MapnikLayerStoreMetadata( workspace, location, this );
    }

    @Override
    public URL getSchema() {
        return SCHEMA_URL;
    }

}
