import type { AxiosRequestConfig } from "axios";
import { BASE_URL } from "../utils/system";
import { requestBackend } from "./requests";

export function findPageRequest(page: number, name: string, size = 12, categoryId: number, sort = "name") {


    const config: AxiosRequestConfig = {

        method: "GET",
        baseURL: BASE_URL,
        url: "/products",
        params: {
            page,
            name,
            size: size,
            categoryId,
            sort: sort

        }
    }

    return requestBackend(config);

}


export function findById(id: number) {

    const config: AxiosRequestConfig = {

        method: "GET",
        baseURL: BASE_URL,
        url: `/products/${id}`,
        params: id
        


    }
    
    return requestBackend(config);
}