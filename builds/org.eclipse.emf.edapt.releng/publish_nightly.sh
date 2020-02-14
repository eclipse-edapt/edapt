#!/bin/sh
###############################################################################
# Copyright (c) 2013 NetXForge.com and others. 
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License 2.0
# which accompanies this distribution, and is available at
# https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Christophe Bouhier - Initial contribution. 
###############################################################################

# https://bugs.eclipse.org/bugs/show_bug.cgi?id=412847
# Edapt Publishing Script
# Publish various artifacts. 
# Edapt P2 => build.eclipse.org/ ~/downloads/edapt/p2

rm -r /home/data/httpd/download.eclipse.org/edapt/p2/nightly/*
cp  -r builds/org.eclipse.emf.edapt.releng.p2/target/repository/* /home/data/httpd/download.eclipse.org/edapt/p2/nightly
