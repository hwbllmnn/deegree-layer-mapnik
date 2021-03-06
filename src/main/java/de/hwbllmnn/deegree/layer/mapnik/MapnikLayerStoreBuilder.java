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

import static org.deegree.commons.ows.metadata.DescriptionConverter.fromJaxb;
import static org.deegree.geometry.metadata.SpatialMetadataConverter.fromJaxb;
import static org.deegree.layer.config.ConfigUtils.parseLayerOptions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.deegree.commons.ows.metadata.Description;
import org.deegree.commons.utils.DoublePair;
import org.deegree.geometry.metadata.SpatialMetadata;
import org.deegree.layer.Layer;
import org.deegree.layer.config.ConfigUtils;
import org.deegree.layer.metadata.LayerMetadata;
import org.deegree.layer.persistence.LayerStore;
import org.deegree.layer.persistence.MultipleLayerStore;
import org.deegree.layer.persistence.base.jaxb.ScaleDenominatorsType;
import org.deegree.rendering.r2d.context.MapOptions;
import org.deegree.workspace.ResourceBuilder;
import org.deegree.workspace.ResourceInitException;
import org.deegree.workspace.ResourceMetadata;

import de.hwbllmnn.deegree.layer.mapnik.jaxb.MapnikLayerType;
import de.hwbllmnn.deegree.layer.mapnik.jaxb.MapnikLayers;

/**
 * This class is responsible for building feature layer stores.
 * 
 * @author <a href="mailto:schmitz@occamlabs.de">Andreas Schmitz</a>
 * 
 * @since 3.4
 */
public class MapnikLayerStoreBuilder implements ResourceBuilder<LayerStore> {

    private MapnikLayers config;

    private ResourceMetadata<LayerStore> metadata;

    public MapnikLayerStoreBuilder( MapnikLayers config, ResourceMetadata<LayerStore> metadata ) {
        this.config = config;
        this.metadata = metadata;
    }

    @Override
    public LayerStore build() {
        try {
            Map<String, Layer> map = new LinkedHashMap<String, Layer>();
            for ( MapnikLayerType cfg : config.getMapnikLayer() ) {
                MapOptions opts = parseLayerOptions( cfg.getLayerOptions() );
                Description desc = fromJaxb( cfg.getTitle(), cfg.getAbstract(), cfg.getKeywords() );

                SpatialMetadata smd = fromJaxb( cfg.getEnvelope(), cfg.getCRS() );

                LayerMetadata md = new LayerMetadata( cfg.getName(), desc, smd );
                md.setMapOptions( ConfigUtils.parseLayerOptions( cfg.getLayerOptions() ) );
                ScaleDenominatorsType sd = cfg.getScaleDenominators();
                if ( sd != null ) {
                    DoublePair p = new DoublePair( sd.getMin(), sd.getMax() );
                    md.setScaleDenominators( p );
                }
                md.setMetadataId( cfg.getMetadataSetId() );
                md.setMapOptions( opts );
                Layer l = new MapnikLayer( md, metadata.getLocation().resolveToFile( cfg.getMapnikConfiguration() ) );
                map.put( cfg.getName(), l );
            }
            return new MultipleLayerStore( map, metadata );
        } catch ( Exception e ) {
            throw new ResourceInitException( "Could not parse layer configuration file: " + e.getLocalizedMessage(), e );
        }
    }

}
