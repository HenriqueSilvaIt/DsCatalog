import type { AxiosRequestConfig } from "axios";
import { BASE_URL } from "../utils/system";
import { requestBackend } from "./requests";


export  function findAll() {


    const config : AxiosRequestConfig = { 
        method: "GET",
        baseURL: BASE_URL,
        url: "/categories"
    
    }

    return  requestBackend(config);

}