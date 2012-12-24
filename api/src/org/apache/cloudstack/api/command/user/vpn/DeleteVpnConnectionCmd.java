// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
package org.apache.cloudstack.api.command.user.vpn;

import org.apache.cloudstack.api.response.Site2SiteVpnConnectionResponse;
import org.apache.log4j.Logger;

import org.apache.cloudstack.api.ApiConstants;
import org.apache.cloudstack.api.BaseAsyncCmd;
import org.apache.cloudstack.api.BaseCmd;
import org.apache.cloudstack.api.Implementation;
import org.apache.cloudstack.api.Parameter;
import org.apache.cloudstack.api.ServerApiException;
import org.apache.cloudstack.api.response.SuccessResponse;
import com.cloud.event.EventTypes;
import com.cloud.exception.ResourceUnavailableException;
import com.cloud.network.Site2SiteVpnConnection;
import com.cloud.user.Account;

@Implementation(description="Delete site to site vpn connection", responseObject=SuccessResponse.class)
public class DeleteVpnConnectionCmd extends BaseAsyncCmd {
    public static final Logger s_logger = Logger.getLogger(DeleteVpnConnectionCmd.class.getName());

    private static final String s_name = "deletevpnconnectionresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////
    @Parameter(name=ApiConstants.ID, type=CommandType.UUID, entityType=Site2SiteVpnConnectionResponse.class,
            required=true, description="id of vpn connection")
    private Long id;

    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public String getEntityTable() {
        return "s2s_vpn_connection";
    }

    public Long getId() {
        return id;
    }

    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////


    @Override
    public String getCommandName() {
        return s_name;
    }

    @Override
    public long getEntityOwnerId() {
        Site2SiteVpnConnection conn = _entityMgr.findById(Site2SiteVpnConnection.class, getId());
        if (conn != null) {
            return conn.getAccountId();
        }
        return Account.ACCOUNT_ID_SYSTEM;
    }

    @Override
    public String getEventDescription() {
        return "Delete site-to-site VPN connection for account " + getEntityOwnerId();
    }

    @Override
    public String getEventType() {
        return EventTypes.EVENT_S2S_VPN_CONNECTION_DELETE;
    }

    @Override
    public void execute(){
        try {
            boolean result = _s2sVpnService.deleteVpnConnection(this);
            if (result) {
                SuccessResponse response = new SuccessResponse(getCommandName());
                this.setResponseObject(response);
            } else {
                throw new ServerApiException(BaseCmd.INTERNAL_ERROR, "Failed to delete site to site VPN connection");
            }
        } catch (ResourceUnavailableException ex) {
            s_logger.warn("Exception: ", ex);
            throw new ServerApiException(BaseCmd.RESOURCE_UNAVAILABLE_ERROR, ex.getMessage());
        }
    }
}
