package com.hackvg.model.entities;

/**
 * Class that respresents the configuration in the model layer,
 *
 * The configuration gets the system wide configuration information. Some elements of the API
 * require some knowledge of this configuration data. The purpose of this is to try and keep the
 * actual API responses as light as possible.
 * It is recommended you cache this data every few days.
 */
public class ConfigurationResponse {

    private ConfigurationImages images;

    public ConfigurationImages getImages() {

        return images;
    }

    class ConfigurationImages {

        private String base_url;
        private String secure_base_url;

        public String getBase_url() {

            return base_url;
        }

        public String getSecure_base_url() {

            return secure_base_url;
        }
    }
}


