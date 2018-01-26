#!/bin/sh
###############################################################################
# Copyright (c) 2013 NetXForge.com and others. 
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#     Christophe Bouhier - Initial contribution. 
###############################################################################

# https://bugs.eclipse.org/bugs/show_bug.cgi?id=412847
# Edapt Publishing Script
# Publish various artifacts. 
# Edapt P2 => build.eclipse.org/ ~/downloads/edapt/p2
#
# SSH Authentication: 
# 		Add -o IdentityFile=/Users/Christophe/.ssh/id_rsa
cp  -r builds/org.eclipse.emf.edapt.releng.p2/target/repository /opt/public//download-staging.priv/modeling/edapt/repository