import type { AxiosRequestConfig } from "axios";
import axios from "axios";
import { BASE_URL } from "../utils/system";


export function requestBackend(config: AxiosRequestConfig) {

   const headers = config.headers
   
    return axios({...config, baseURL: BASE_URL, headers})

}