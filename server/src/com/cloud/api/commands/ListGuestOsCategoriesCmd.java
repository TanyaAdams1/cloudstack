/**
 *  Copyright (C) 2010 Cloud.com, Inc.  All rights reserved.
 * 
 * This software is licensed under the GNU General Public License v3 or later.
 * 
 * It is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package com.cloud.api.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloud.api.BaseListCmd;
import com.cloud.api.Implementation;
import com.cloud.api.Parameter;
import com.cloud.api.response.GuestOSCategoryResponse;
import com.cloud.api.response.ListResponse;
import com.cloud.storage.GuestOSCategoryVO;

@Implementation(method="listGuestOSCategoriesByCriteria", description="Lists all supported OS categories for this cloud.")
public class ListGuestOsCategoriesCmd extends BaseListCmd {
    public static final Logger s_logger = Logger.getLogger(ListIsosCmd.class.getName());

    private static final String s_name = "listoscategoriesresponse";

    /////////////////////////////////////////////////////
    //////////////// API parameters /////////////////////
    /////////////////////////////////////////////////////

    @Parameter(name="id", type=CommandType.LONG)
    private Long id;


    /////////////////////////////////////////////////////
    /////////////////// Accessors ///////////////////////
    /////////////////////////////////////////////////////

    public Long getId() {
        return id;
    }


    /////////////////////////////////////////////////////
    /////////////// API Implementation///////////////////
    /////////////////////////////////////////////////////

    @Override
    public String getName() {
        return s_name;
    }

    @Override @SuppressWarnings("unchecked")
    public ListResponse<GuestOSCategoryResponse> getResponse() {
        List<GuestOSCategoryVO> osCategories = (List<GuestOSCategoryVO>)getResponseObject();

        ListResponse<GuestOSCategoryResponse> response = new ListResponse<GuestOSCategoryResponse>();
        List<GuestOSCategoryResponse> osCatResponses = new ArrayList<GuestOSCategoryResponse>();
        for (GuestOSCategoryVO osCategory : osCategories) {
            GuestOSCategoryResponse categoryResponse = new GuestOSCategoryResponse();
            categoryResponse.setId(osCategory.getId());
            categoryResponse.setName(osCategory.getName());

            categoryResponse.setResponseName("oscategory");
            osCatResponses.add(categoryResponse);
        }

        response.setResponses(osCatResponses);
        response.setResponseName(getName());
        return response;
    }
}
