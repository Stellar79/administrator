import axios from "axios"
import router from "./router"
import Element from "element-ui"

axios.defaults.baseURL="http://119.23.242.206:8081"

const request = axios.create({
    timeout: 5000,
    headers: {
      'Content-Type': "application/json; charset=utf-8"
    }
  })

request.interceptors.request.use(config => {
    config.headers['Authorization'] = localStorage.getItem("token")
    return config
    })

    // Add a response interceptor
request.interceptors.response.use(response => {
    console.log("response ->" + response)
    let res = response.data;

    if (res.code === 200){
    // Any status code that lie within the range of 2xx cause this function to trigger
    // Do something with response data
    return response}
    else{
        Element.Message.error(!res.msg? 'System exception!': res.msg)
        return Promise.reject(response.data.msg);
    }

  }, error => {

    console.log(error)
    // Any status codes that falls outside the range of 2xx cause this function to trigger
    if(error.response.data){
     error.Message = error.response.data.msg;
    }
    // No permission
    if(error.response.status === 401){
        router.push("/login")
    }

    Element.Message.error(error.Message, {duration :3000})

    // Do something with response error
    return Promise.reject(error);
  });
  export default request