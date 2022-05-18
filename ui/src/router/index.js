import {createRouter, createWebHashHistory} from 'vue-router'

const routes = [{
    path: '/swagger', name: 'swagger', component: () => import ('../view/swagger-ui'),
}, {
    path: '/swagger-list', name: 'swagger-list', component: () => import ('../view/swagger-list')
}, {
    path: '/flow-list', name: 'flow-list', component: () => import ('../view/flow-list')
}, {
    path: '/flow-ui', name: 'flow-ui', component: () => import ('../view/flow-ui')
}, {
    path: '/result-ui', name: 'result-ui', component: () => import ('../view/result-ui')
}, {
    path: '/watcher-ui', name: 'watcher-ui', component: () => import ('../view/watcher-ui')
}];

const router = createRouter({
    history: createWebHashHistory(), routes
})

export default router;
