-include= ~${workspace}/cnf/resources/bnd/feature.props
symbolicName=io.openliberty.jdbc4.3.internal.ee-9.0
singleton=true
-features=\
 com.ibm.websphere.appserver.transaction-2.0
-bundles=\
 com.ibm.ws.jdbc.jakarta,\
 com.ibm.ws.jdbc.4.1.jakarta,\
 com.ibm.ws.jdbc.4.2.jakarta,\
 com.ibm.ws.jdbc.4.3.jakarta
kind=ga
edition=core
WLP-Activation-Type: parallel
