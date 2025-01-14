-include= ~${workspace}/cnf/resources/bnd/feature.props
symbolicName=com.ibm.websphere.appserver.mpConfig-1.3
WLP-DisableAllFeatures-OnConflict: false
visibility=public
singleton=true
IBM-App-ForceRestart: install, \
 uninstall
IBM-API-Package: \
  org.eclipse.microprofile.config;  type="stable", \
  org.eclipse.microprofile.config.spi;  type="stable", \
  org.eclipse.microprofile.config.inject;  type="stable"
IBM-ShortName: mpConfig-1.3
Subsystem-Name: MicroProfile Config 1.3
-features=com.ibm.websphere.appserver.appmanager-1.0, \
  io.openliberty.mpCompatible-0.0, \
  com.ibm.websphere.appserver.javax.annotation-1.2; ibm.tolerates:="1.3", \
  com.ibm.websphere.appserver.javax.cdi-1.2; ibm.tolerates:="2.0", \
  com.ibm.websphere.appserver.org.eclipse.microprofile.config-1.3
-bundles=io.openliberty.microprofile.config.internal.common, \
 io.openliberty.microprofile.config.internal.serverxml, \
 com.ibm.ws.microprofile.config.1.1, \
 com.ibm.ws.microprofile.config.1.2, \
 com.ibm.ws.microprofile.config.1.3, \
 com.ibm.ws.microprofile.config.1.3.services, \
 com.ibm.ws.cdi.interfaces, \
 com.ibm.ws.org.apache.commons.lang3
kind=ga
edition=core
WLP-Activation-Type: parallel
WLP-Platform: microProfile-1.4,microProfile-2.0,microProfile-2.1,microProfile-2.2,microProfile-3.0,microProfile-3.2
